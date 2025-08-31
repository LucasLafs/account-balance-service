package com.challenge.accountbalanceservice.domain.service

import com.challenge.accountbalanceservice.domain.gateways.FinancialTransactionStorageGateway
import com.challenge.accountbalanceservice.domain.services.FinancialTransactionProcessService
import com.challenge.accountbalanceservice.factories.domain.entities.FinancialTransactionFactory
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class FinancialTransactionProcessServiceTest {
    private val financialTransactionStorageGateway = mockk<FinancialTransactionStorageGateway>()
    private val service = FinancialTransactionProcessService(
        financialTransactionStorageGateway = financialTransactionStorageGateway
    )

    @Test
    fun `should process financial transaction when receive a list of financial transaction`() {
        val financialTransactions = listOf(FinancialTransactionFactory.buildSimpleFixture())

        every {
            financialTransactionStorageGateway.upsertAll(financialTransactions)
        } just runs

        assertDoesNotThrow { service.process(financialTransactions) }

        verify(exactly = 1) { financialTransactionStorageGateway.upsertAll(any()) }
    }
}
