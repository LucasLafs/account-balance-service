package com.challenge.accountbalanceservice.resources.jpa

import com.challenge.accountbalanceservice.factories.domain.entities.AccountBalanceFactory
import com.challenge.accountbalanceservice.resources.repositories.AccountJpaRepository
import com.challenge.accountbalanceservice.resources.repositories.tables.toTable
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.Optional
import kotlin.test.assertEquals

internal class AccountStorageGatewayJpaTest {
    private val accountJpaRepository = mockk<AccountJpaRepository>()
    private val repository = AccountStorageGatewayJpa(
        accountRepository = accountJpaRepository
    )

    @Test
    fun `should success to retrieve account balance from database`() {
        val accountId = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975"
        val accountBalance = AccountBalanceFactory.buildSimpleFixture()

        every { accountJpaRepository.findById(accountId) } returns Optional.of(accountBalance.toTable())

        val response = assertDoesNotThrow { repository.findById(accountId) }

        assertEquals(accountBalance, response)

        verify(exactly = 1) { accountJpaRepository.findById(any()) }
    }
}
