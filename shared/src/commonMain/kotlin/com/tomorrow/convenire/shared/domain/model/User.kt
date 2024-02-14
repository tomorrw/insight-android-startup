package com.tomorrow.convenire.shared.domain.model

import com.tomorrow.convenire.shared.domain.utils.PhoneNumber
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class User(
    val id: String,
    val uuid: String,
    val salutation: Salutation = Salutation.None,
    val name: String,
    val email: Email?,
    val phoneNumber: PhoneNumber,
    val hasPaid: Boolean = false,
    val league: League,
    val nextLeagueName: String,
    val actions: List<Action>,
    val notificationTopics: List<String> = listOf(),
) {
    fun getFullName() = this.name.split(" ")
        .joinToString(" ") { name -> name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }

    fun getFormattedName() = if (salutation == Salutation.None) getFullName()
    else "${salutation.getAsString()}. ${getFullName()}"

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
