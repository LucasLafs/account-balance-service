package com.challenge.accountbalanceservice.factories.domain.entities

import com.challenge.accountbalanceservice.domain.entities.AccountBalance
import com.challenge.accountbalanceservice.domain.entities.AccountStatus
import com.challenge.accountbalanceservice.domain.entities.Balance
import com.challenge.accountbalanceservice.domain.entities.FinancialTransaction
import com.challenge.accountbalanceservice.domain.entities.Transaction
import com.challenge.accountbalanceservice.domain.entities.TransactionStatus
import com.challenge.accountbalanceservice.domain.entities.TransactionType
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.Currency

internal object FinancialTransactionFactory {
    fun buildSimpleFixture(): FinancialTransaction {
        return FinancialTransaction(
            transaction = Transaction(
                transactionId = "8e8ae808-b154-48b5-9f3e-553935cc4543",
                accountId = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
                type = TransactionType.CREDIT,
                status = TransactionStatus.APPROVED,
                amount = BigDecimal.TEN,
                currency = Currency.getInstance("BRL"),
                createdAt = OffsetDateTime.now()
            ),
            account = AccountBalance(
                accountId = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
                owner = "315e3cfe-f4af-4cd2-b298-a449e614349a",
                status = AccountStatus.ENABLED,
                balance = Balance(
                    amount = BigDecimal.TEN,
                    currency = Currency.getInstance("BRL")
                ),
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now()
            )
        )
    }
}
