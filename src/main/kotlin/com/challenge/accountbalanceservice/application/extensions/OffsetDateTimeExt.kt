package com.challenge.accountbalanceservice.application.extensions

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

@SuppressWarnings("MagicNumber")
fun microsToOffsetDateTime(micros: Long, offsetHours: Int = -3): OffsetDateTime {
    val instant = Instant.ofEpochSecond(
        micros / 1_000_000,
        (micros % 1_000_000) * 1_000
    )
    return instant.atOffset(ZoneOffset.ofHours(offsetHours))
}

@SuppressWarnings("MagicNumber")
fun secondsToOffsetDateTime(seconds: Long, offsetHours: Int = -3): OffsetDateTime {
    return Instant.ofEpochSecond(seconds)
        .atOffset(ZoneOffset.ofHours(offsetHours))
}
