package com.challenge.accountbalanceservice.application.controllers

import com.challenge.accountbalanceservice.application.web.controllers.AccountBalanceController
import com.challenge.accountbalanceservice.application.web.extensions.toDto
import com.challenge.accountbalanceservice.domain.exceptions.AccountNotFoundException
import com.challenge.accountbalanceservice.domain.services.RetrieveAccountBalanceService
import com.challenge.accountbalanceservice.factories.domain.entities.AccountBalanceFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class AccountBalanceControllerTest {
    private val retrieveAccountBalanceService = mockk<RetrieveAccountBalanceService>()
    private val controller = AccountBalanceController(
        retrieveAccountBalanceService = retrieveAccountBalanceService
    )

    @Test
    fun `given a valid account balance id when try retrieve account balance should successfully`() {
        val accountId = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975"
        val accountBalance = AccountBalanceFactory.buildSimpleFixture()

        every { retrieveAccountBalanceService.getAccountBalanceById(accountId) } returns accountBalance

        val response = assertDoesNotThrow { controller.getAccountBalance(accountId) }

        assertEquals(accountBalance.toDto(), response.body)

        verify(exactly = 1) { retrieveAccountBalanceService.getAccountBalanceById(any()) }
    }

    @Test
    fun `given a invalid account balance id when try retrieve account balance should returns error response`() {
        val accountId = "1"

        every { retrieveAccountBalanceService.getAccountBalanceById(accountId) } throws AccountNotFoundException()

        val response = assertThrows<AccountNotFoundException> { controller.getAccountBalance(accountId) }

        assertEquals("ACCOUNT_NOT_FOUND", response.type)

        verify(exactly = 1) { retrieveAccountBalanceService.getAccountBalanceById(any()) }
    }
}
