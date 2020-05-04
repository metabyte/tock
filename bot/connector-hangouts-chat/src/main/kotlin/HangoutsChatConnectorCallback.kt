package ai.tock.bot.connector.hangoutschat

import ai.tock.bot.connector.ConnectorCallbackBase


data class HangoutsChatConnectorCallback(
    override val applicationId: String,
    val spaceName: String,
    val threadName: String
) : ConnectorCallbackBase(applicationId, hangoutsChatConnectorType) {
}