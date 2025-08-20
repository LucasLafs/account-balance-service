package com.challenge.accountbalanceservice.domain.entities

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.Currency

data class AccountBalance(
    val id: String,
    val owner: String,
    val status: AccountStatus,
    val balance: Balance,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

data class Balance(
    val amount: BigDecimal,
    val currency: Currency
)

enum class AccountStatus {
    ENABLED,
    DISABLED
}
