package com.tomorrow.convenire.shared.domain.model

data class FullName(
    val salutation: Salutation = Salutation.None,
    val firstName: String,
    val lastName: String
){
    fun getFullName() = "$firstName $lastName"

    fun getFormattedName() = if (salutation == Salutation.None) getFullName()
    else "${salutation.getAsString()} ${getFullName()}"
}
