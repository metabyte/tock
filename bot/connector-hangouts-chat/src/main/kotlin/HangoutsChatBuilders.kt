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

import ai.tock.bot.connector.ConnectorMessage
import ai.tock.bot.connector.ConnectorType
import ai.tock.bot.engine.Bus
import ai.tock.bot.engine.I18nTranslator

internal const val HANGOUTS_CHAT_CONNECTOR_TYPE_ID = "hangouts_chat"

val hangoutsChatConnectorType = ConnectorType(HANGOUTS_CHAT_CONNECTOR_TYPE_ID)

/**
 * Sends a Hangouts Chat message only if the [ConnectorType] of the current [BotBus] is [hangoutsChatConnectorType].
 */
fun <T : Bus<T>> T.sendToHangoutsChat(
    delay: Long = defaultDelay(currentAnswerIndex),
    messageProvider: T.() -> HangoutsChatConnectorMessage
): T {
    if (targetConnectorType == hangoutsChatConnectorType) {
        withMessage(messageProvider(this))
        send(delay)
    }
    return this
}

/**
 * Sends a Hangouts Chat message as last bot answer, only if the [ConnectorType] of the current [BotBus] is [hangoutsChatConnectorType].
 */
fun <T : Bus<T>> T.endForHangoutsChat(
    delay: Long = defaultDelay(currentAnswerIndex),
    messageProvider: T.() -> HangoutsChatConnectorMessage
): T {
    if (targetConnectorType == hangoutsChatConnectorType) {
        withMessage(messageProvider(this))
        end(delay)
    }
    return this
}

/**
 * Adds a Hangouts Chat [ConnectorMessage] if the current connector is [hangoutsChatConnectorType].
 * You need to call [BotBus.send] or [BotBus.end] later to send this message.
 */
fun <T : Bus<T>> T.withHangoutsChat(messageProvider: () -> HangoutsChatConnectorMessage): T {
    return withMessage(hangoutsChatConnectorType, messageProvider)
}

fun I18nTranslator.textMessage(message: CharSequence): HangoutsChatMessageOut {
    return HangoutsChatMessageOut(translate(message).toString())
}