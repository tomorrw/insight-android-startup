package com.tomorrow.convenire.shared.domain.model

class AppConfig(
    val version: String,
    val name: String,
    val platform: AppPlatform,
    val updateUrl: String?,
)