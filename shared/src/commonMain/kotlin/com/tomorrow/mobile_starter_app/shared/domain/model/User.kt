package com.tomorrow.mobile_starter_app.shared.domain.model

import com.tomorrow.kmmProjectStartup.domain.model.Email
import com.tomorrow.kmmProjectStartup.domain.model.FullName
import com.tomorrow.kmmProjectStartup.domain.utils.PhoneNumber
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class User(
    val id: String,
    val uuid: String,
    var fullName: FullName,
    val email: Email?,
    val phoneNumber: PhoneNumber,
    val notificationTopics: List<String> = listOf(),
) {

    @Serializable
    private class QrCodeContent(
        val uuid: String,
        @SerialName("epoch_seconds")
        val epochSeconds: String
    )

    fun generateQrCodeString(): String {
        return Json.encodeToString(
            QrCodeContent(
                uuid = this.uuid,
                epochSeconds = Clock.System.now().epochSeconds.toString()
            )
        )
    }
}
