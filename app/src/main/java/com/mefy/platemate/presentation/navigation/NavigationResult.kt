package com.mefy.platemate.presentation.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

fun <T> SavedStateHandle.setOneShotResult(key: String, value: T) {
    this[key] = value
}

inline fun <reified T> SavedStateHandle.consumeOneShotResult(key: String): T? {
    val value = get<T>(key)
    if (value != null || contains(key)) {
        remove<T>(key)
    }
    return value
}

fun <T> NavController.setResult(key: String, value: T) {
    previousBackStackEntry?.savedStateHandle?.setOneShotResult(key = key, value = value)
}

inline fun <reified T> NavBackStackEntry.consumeOneShotResult(key: String): T? =
    savedStateHandle.consumeOneShotResult<T>(key = key)
