package com.challenge.accountbalanceservice.factories.domain.entities

import com.challenge.accountbalanceservice.domain.entities.AccountBalance
import com.challenge.accountbalanceservice.domain.entities.AccountStatus
import com.challenge.accountbalanceservice.domain.entities.Balance
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.Currency

internal object AccountBalanceFactory {
    fun buildSimpleFixture(): AccountBalance {
        return AccountBalance(
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
    }
}
