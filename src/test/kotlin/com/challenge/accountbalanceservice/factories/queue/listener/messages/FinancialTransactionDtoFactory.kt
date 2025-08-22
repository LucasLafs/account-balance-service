package com.challenge.accountbalanceservice.factories.queue.listener.messages

import com.challenge.accountbalanceservice.application.queue.listener.messages.AccountDto
import com.challenge.accountbalanceservice.application.queue.listener.messages.BalanceDto
import com.challenge.accountbalanceservice.application.queue.listener.messages.FinancialTransactionDto
import com.challenge.accountbalanceservice.application.queue.listener.messages.TransactionDto
import com.challenge.accountbalanceservice.domain.entities.AccountStatus
import com.challenge.accountbalanceservice.domain.entities.TransactionStatus
import com.challenge.accountbalanceservice.domain.entities.TransactionType
import java.math.BigDecimal

internal object FinancialTransactionDtoFactory {
    fun buildSimpleFixture(): FinancialTransactionDto {
        return FinancialTransactionDto(
            transaction = TransactionDto(
                id = "8e8ae808-b154-48b5-9f3e-553935cc4543",
                type = TransactionType.CREDIT,
                status = TransactionStatus.APPROVED,
                amount = BigDecimal.TEN,
                currency = "BRL",
                timestamp = 1751641364589998
            ),
            account = AccountDto(
                id = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975",
                owner = "315e3cfe-f4af-4cd2-b298-a449e614349a",
                createdAt = 1634874339,
                status = AccountStatus.ENABLED,
                balance = BalanceDto(
                    amount = BigDecimal.TEN,
                    currency = "BRL"
                )
            )
        )
    }
}
