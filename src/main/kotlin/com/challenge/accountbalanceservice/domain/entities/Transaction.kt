package com.challenge.accountbalanceservice.domain.entities

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.Currency

data class Transaction(
    val transactionId: String,
    val accountId: String,
    val type: TransactionType,
    val status: TransactionStatus,
    val amount: BigDecimal,
    val currency: Currency,
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
