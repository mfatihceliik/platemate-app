package com.mefy.platemate.data.local.room

import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Ids for locally-created (not-yet-acked) chat message rows. Always negative so they can never
 * collide with a real, positive, server-assigned message id. An in-process monotonic counter —
 * not [System.nanoTime]/[System.currentTimeMillis] alone — because two rapid-fire sends can tie
 * within the same millisecond, which is exactly the scenario this needs to stay unique under.
 */
@Singleton
class TempMessageIdGenerator @Inject constructor() {
    private val counter = AtomicLong(-System.currentTimeMillis())

    fun nextId(): Long = counter.getAndDecrement()
}
