package com.challenge.accountbalanceservice.domain.entities

import java.math.BigDecimal
import java.time.OffsetDateTime

data class Transaction(
    val id: String,
    val type: TransactionType,
    val status: TransactionStatus,
    val amount: BigDecimal,
    val currency: String,
    val createdAt: OffsetDateTime
)

enum class TransactionType {
    CREDIT,
    DEBIT
}

enum class TransactionStatus {
    APPROVED,
    REJECTED
}
