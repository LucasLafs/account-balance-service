package com.challenge.accountbalanceservice.resources.repositories.tables

import com.challenge.accountbalanceservice.domain.entities.Transaction
import com.challenge.accountbalanceservice.domain.entities.TransactionStatus
import com.challenge.accountbalanceservice.domain.entities.TransactionType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.domain.Persistable
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity(name = "Transactions")
@Table(name = "transactions")
data class TransactionTable(
    @Id
    val transactionId: String,

    @Enumerated(EnumType.STRING)
    val type: TransactionType,

    @Enumerated(EnumType.STRING)
    val status: TransactionStatus,

    val amount: BigDecimal,
    val currency: String,
    val createdAt: OffsetDateTime,

    @Transient
    val new: Boolean
) : Persistable<String> {
    override fun isNew() = new
    override fun getId() = transactionId
}

fun Transaction.toTable(isNew: Boolean = true): TransactionTable {
    return TransactionTable(
        transactionId = id,
        type = type,
        status = status,
        amount = amount,
        currency = currency,
        createdAt = createdAt,
        new = isNew
    )
}

fun TransactionTable.toEntity(): Transaction {
    return Transaction(
        id = transactionId,
        type = type,
        status = status,
        amount = amount,
        currency = currency,
        createdAt = createdAt
    )
}
