package com.mefy.platemate.presentation.common.error

sealed class ErrorContext {
    object Generic : ErrorContext()
    object Login : ErrorContext()
    object Register : ErrorContext()
    object Search : ErrorContext()
    object Profile : ErrorContext()
    object SessionGate : ErrorContext()
}
