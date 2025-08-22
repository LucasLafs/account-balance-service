package com.challenge.accountbalanceservice.factories.resources.repositories.tables

import com.challenge.accountbalanceservice.domain.entities.TransactionStatus
import com.challenge.accountbalanceservice.domain.entities.TransactionType
import com.challenge.accountbalanceservice.resources.repositories.tables.TransactionTable
import java.math.BigDecimal
import java.time.OffsetDateTime

internal object TransactionTableFactory {
    fun buildSimpleFixture(
        status: TransactionStatus = TransactionStatus.APPROVED,
        type: TransactionType = TransactionType.CREDIT
    ): TransactionTable {
        return TransactionTable(
            transactionId = "8e8ae808-b154-48b5-9f3e-553935cc4543",
            accountId = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
            type = type,
            status = status,
            amount = BigDecimal.TEN,
            currency = "BRL",
            createdAt = OffsetDateTime.now()
        )
    }
}
