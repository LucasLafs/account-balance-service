package com.challenge.accountbalanceservice.resources.jpa

import com.challenge.accountbalanceservice.factories.domain.entities.FinancialTransactionFactory
import com.challenge.accountbalanceservice.factories.resources.repositories.tables.AccountTableFactory
import com.challenge.accountbalanceservice.factories.resources.repositories.tables.TransactionTableFactory
import com.challenge.accountbalanceservice.resources.repositories.FinancialTransactionJdbcRepository
import com.challenge.accountbalanceservice.resources.repositories.tables.AccountTable
import com.challenge.accountbalanceservice.resources.repositories.tables.TransactionTable
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals

internal class FinancialTransactionStorageGatewayJpaTest {
    val financialTransactionJdbcRepository = mockk<FinancialTransactionJdbcRepository>()
    val gateway = FinancialTransactionStorageGatewayJpa(
        repository = financialTransactionJdbcRepository
    )

    @Test
    fun `should insert all financial transactions successfully when is called`() {
        val financialTransactions = listOf(FinancialTransactionFactory.buildSimpleFixture())
        val accountTables = listOf(AccountTableFactory.buildSimpleFixture())
        val transactionTables = listOf(TransactionTableFactory.buildSimpleFixture())
        val slotAccountTable = slot<List<AccountTable>>()
        val slotTransactionTable = slot<List<TransactionTable>>()

        every {
            financialTransactionJdbcRepository.insertOrUpdate(
                capture(slotAccountTable),
                capture(slotTransactionTable)
            )
        } just runs

        assertDoesNotThrow { gateway.insertOrUpdate(financialTransactions) }

        val slotAccountCaptured = slotAccountTable.captured.single()
        val slotTransactionCaptured = slotTransactionTable.captured.single()

        assertEquals(accountTables.first().accountId, slotAccountCaptured.accountId)
        assertEquals(transactionTables.first().transactionId, slotTransactionCaptured.transactionId)
        assertEquals(transactionTables.first().accountId, slotAccountCaptured.accountId)

        verify(exactly = 1) {
            financialTransactionJdbcRepository.insertOrUpdate(any(), any())
        }
    }
}
