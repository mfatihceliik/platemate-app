package com.mefy.platemate.core.coroutine

import javax.inject.Qualifier

/** Süreç ömrü boyunca yaşayan uygulama-geneli [kotlinx.coroutines.CoroutineScope] niteleyicisi. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope
