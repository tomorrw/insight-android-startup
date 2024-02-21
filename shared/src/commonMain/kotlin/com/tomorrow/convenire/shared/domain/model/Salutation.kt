package com.tomorrow.convenire.shared.domain.model

enum class Salutation {
    Mr,
    Ms,
    Mrs,
    Miss,
    Madam,
    Dr,
    Sir,
    Pr,
    None,
    Esq;

    fun getAsString(): String? = if (this == None) null else this.name
}