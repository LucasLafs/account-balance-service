package com.challenge.accountbalanceservice.testing.factory

import com.challenge.accountbalanceservice.domain.entities.AccountStatus
import com.challenge.accountbalanceservice.resources.repositories.tables.AccountTable
import java.math.BigDecimal
import java.time.OffsetDateTime

internal object AccountTableFactory {
    fun buildSimpleFixture(
        accountId: String = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa974",
        owner: String = "315e3cfe-f4af-4cd2-b298-a449e614349a",
        status: AccountStatus = AccountStatus.ENABLED
    ): AccountTable {
        return AccountTable(
            accountId = accountId,
            owner = owner,
            status = status,
            amount = BigDecimal.TEN,
            currency = "BRL",
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
    }
}
