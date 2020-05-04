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

import ai.tock.bot.connector.*
import ai.tock.shared.resourceAsStream
import ai.tock.shared.resourceAsString
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.chat.v1.HangoutsChat
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import kotlin.reflect.KClass

private const val CHAT_SCOPE = "https://www.googleapis.com/auth/chat.bot"
private const val SERVICE_CREDENTIAL_PATH = "https://www.googleapis.com/auth/chat.bot"

internal object HangoutsChatConnectorProvider : ConnectorProvider {

    override val connectorType: ConnectorType get() = hangoutsChatConnectorType

    override fun connector(connectorConfiguration: ConnectorConfiguration): Connector {
        with(connectorConfiguration) {

            val requestInitializer: HttpRequestInitializer = HttpCredentialsAdapter(loadCredentials(
                connectorConfiguration.parameters.getOrDefault(
                    SERVICE_CREDENTIAL_PATH,
                    "/service-account-$connectorId.json")
            ))
            val chatService = HangoutsChat.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                requestInitializer)
                .setApplicationName(connectorId)
                .build()

            return HangoutsChatConnector(
                connectorId,
                path,
                chatService
            )
        }
    }

    private fun loadCredentials(credentialResourceFilePath: String): GoogleCredentials =
        ServiceAccountCredentials
            .fromStream(resourceAsStream(credentialResourceFilePath))
            .createScoped(CHAT_SCOPE)

    override fun configuration(): ConnectorTypeConfiguration =
        ConnectorTypeConfiguration(
            hangoutsChatConnectorType, listOf(
            ConnectorTypeConfigurationField(
                "Service account credential file path (default : /service-account-{connectorId}.json)",
                SERVICE_CREDENTIAL_PATH,
                false
            )
        ),
            svgIcon = resourceAsString("/hangouts_chat.svg")
        )

    override val supportedResponseConnectorMessageTypes: Set<KClass<out ConnectorMessage>> = setOf(HangoutsChatMessageOut::class)

}

internal class HangoutsChatConnectorProviderService : ConnectorProvider by HangoutsChatConnectorProvider