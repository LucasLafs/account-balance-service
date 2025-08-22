package com.challenge.accountbalanceservice.domain.service

import com.challenge.accountbalanceservice.domain.exceptions.AccountNotFoundException
import com.challenge.accountbalanceservice.domain.gateways.AccountStorageGateway
import com.challenge.accountbalanceservice.domain.services.RetrieveAccountBalanceService
import com.challenge.accountbalanceservice.factories.domain.entities.AccountBalanceFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class RetrieveAccountBalanceServiceTest {
    private val accountStorageGateway = mockk<AccountStorageGateway>()
    private val service = RetrieveAccountBalanceService(
        accountStorageGateway = accountStorageGateway
    )

    @Test
    fun `given a valid account balance id when try retrieve from database should successfully`() {
        val accountId = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975"
        val accountBalance = AccountBalanceFactory.buildSimpleFixture()

        every { accountStorageGateway.findById(accountId) } returns accountBalance

        val response = assertDoesNotThrow { service.getAccountBalanceById(accountId) }

        assertEquals(accountBalance, response)

        verify(exactly = 1) { accountStorageGateway.findById(any()) }
    }

    @Test
    fun `given a invalid account balance id when try retrieve from database should returns account not found exception`() {
        val accountId = "1"

        every { accountStorageGateway.findById(accountId) } throws AccountNotFoundException()

        assertThrows<AccountNotFoundException> { service.getAccountBalanceById(accountId) }

        verify(exactly = 1) { accountStorageGateway.findById(any()) }
    }
}
