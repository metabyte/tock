/*
 * Copyright (C) 2017/2020 e-voyageurs technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ai.tock.bot.connector.hangoutschat

import ai.tock.bot.connector.ConnectorBase
import ai.tock.bot.connector.ConnectorCallback
import ai.tock.bot.connector.ConnectorData
import ai.tock.bot.connector.ConnectorMessage
import ai.tock.bot.connector.media.MediaMessage
import ai.tock.bot.engine.BotBus
import ai.tock.bot.engine.ConnectorController
import ai.tock.bot.engine.action.Action
import ai.tock.bot.engine.event.Event
import ai.tock.shared.Executor
import ai.tock.shared.injector
import com.github.salomonbrys.kodein.instance
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.chat.v1.HangoutsChat
import com.google.api.services.chat.v1.model.DeprecatedEvent
import com.google.api.services.chat.v1.model.Thread
import mu.KotlinLogging
import java.time.Duration

class HangoutsChatConnector(
    val connectorId: String,
    val path: String,
    val chatService: HangoutsChat
) : ConnectorBase(HangoutsChatConnectorProvider.connectorType) {

    private val logger = KotlinLogging.logger {}
    private val executor: Executor by injector.instance()

    override fun register(controller: ConnectorController) {
        controller.registerServices(path) { router ->
            router.post(path).handler { context ->
                try {
                    val body = context.bodyAsString
                    logger.info { "message received from hangouts chat: $body" }

                    //answer immediately
                    context.response().end()

                    val messageEvent = JacksonFactory().fromString(body, DeprecatedEvent::class.java)
                    val spaceName = messageEvent.space?.name
                    val threadName = messageEvent.message?.thread?.name
                    val event = HangoutsChatRequestConverter.toEvent(messageEvent, connectorId)
                    if (event != null && spaceName != null && threadName != null) {
                        executor.executeBlocking {
                            controller.handle(event, ConnectorData(HangoutsChatConnectorCallback(connectorId, spaceName, threadName)))
                        }
                    } else {
                        logger.debug { "skip message: $body" }
                    }

                } catch (e: Throwable) {
                    logger.error { e }
                }
            }

        }
    }

    override fun send(event: Event, callback: ConnectorCallback, delayInMs: Long) {
        logger.debug { "event: $event" }
        if (event is Action) {
            val message = HangoutsChatMessageConverter.toMessageOut(event)
            if (message != null) {
                callback as HangoutsChatConnectorCallback
                executor.executeBlocking(Duration.ofMillis(delayInMs)) {
                    chatService.spaces().messages().create(callback.spaceName, message.googleMessage.setThread(Thread().setName(callback.threadName)))
                }
            }
        }
    }


    override fun addSuggestions(text: CharSequence, suggestions: List<CharSequence>): BotBus.() -> ConnectorMessage? = {
        TODO("Not yet implemented")
    }

    override fun addSuggestions(message: ConnectorMessage, suggestions: List<CharSequence>): BotBus.() -> ConnectorMessage? = {
        TODO("Not yet implemented")
    }

    override fun toConnectorMessage(message: MediaMessage): BotBus.() -> List<ConnectorMessage> = {
        TODO("Not yet implemented")
    }
}