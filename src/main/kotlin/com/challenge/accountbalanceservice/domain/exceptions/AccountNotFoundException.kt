package com.challenge.accountbalanceservice.domain.exceptions

class AccountNotFoundException(
    override val type: String = "ACCOUNT_NOT_FOUND",
    override val message: String = "The given account id was not found",
    override val cause: Throwable? = null
) : BaseException(
    type = type,
    message = message,
    cause = cause
)
