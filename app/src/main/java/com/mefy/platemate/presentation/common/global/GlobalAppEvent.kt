package com.mefy.platemate.presentation.common.global


sealed interface GlobalAppEvent {

    data object SessionExpired : GlobalAppEvent
}
