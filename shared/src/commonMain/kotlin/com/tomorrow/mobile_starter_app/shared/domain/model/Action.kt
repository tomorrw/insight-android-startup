package com.tomorrow.mobile_starter_app.shared.domain.model

class Action(
    val url: String,
    val type: ActionType,
    val label: String,
    val isPrimary: Boolean,
    val isDisabled: Boolean,
){
    enum class ActionType { OpenLink, ApiHit }
}