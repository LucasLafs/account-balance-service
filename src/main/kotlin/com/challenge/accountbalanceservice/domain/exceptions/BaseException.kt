package com.challenge.accountbalanceservice.domain.exceptions

import java.lang.RuntimeException

open class BaseException(
    open val type: String,
    override val message: String = "Internal Server Error",
    override val cause: Throwable? = null
) : RuntimeException(message, cause)
