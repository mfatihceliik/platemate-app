package com.mefy.platemate.core.coroutine

import kotlinx.coroutines.CoroutineDispatcher

data class AppDispatchers(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val default: CoroutineDispatcher
)
