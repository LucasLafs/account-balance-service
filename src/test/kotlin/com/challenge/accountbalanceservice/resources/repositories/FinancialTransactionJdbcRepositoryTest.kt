package com.challenge.accountbalanceservice.resources.repositories

import com.challenge.accountbalanceservice.domain.entities.AccountStatus.DISABLED
import com.challenge.accountbalanceservice.factories.resources.repositories.tables.AccountTableFactory
import com.challenge.accountbalanceservice.factories.resources.repositories.tables.TransactionTableFactory
import io.micrometer.core.instrument.MeterRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import java.sql.Statement.SUCCESS_NO_INFO

internal class FinancialTransactionJdbcRepositoryTest {
    private val namedParameterJdbcTemplate = mockk<NamedParameterJdbcTemplate>()
    private val meterRegistry = mockk<MeterRegistry>(relaxed = true)
    val financialTransactionJdbcRepository = FinancialTransactionJdbcRepository(
        jdbcTemplate = namedParameterJdbcTemplate,
        meterRegistry = meterRegistry
    )

    @Test
    fun `should correctly insert or update financial transaction`() {
        val accountTable = listOf(
            AccountTableFactory.buildSimpleFixture(),
            AccountTableFactory.buildSimpleFixture(
                accountId = "315e3cfe-f4af-4cd2-b298-a449e614349a",
                status = DISABLED
            )
        )
        val transactionTable = listOf(
            TransactionTableFactory.buildSimpleFixture()
        )

        val sqlSlot = slot<Array<SqlParameterSource>>()

        every {
            namedParameterJdbcTemplate.batchUpdate(any(), capture(sqlSlot))
        } returnsMany listOf(intArrayOf(SUCCESS_NO_INFO, SUCCESS_NO_INFO))

        assertDoesNotThrow {
            financialTransactionJdbcRepository.insertOrUpdate(accountTable, transactionTable)
        }

        verify(exactly = 2) {
            namedParameterJdbcTemplate.batchUpdate(any<String>(), any<Array<MapSqlParameterSource>>())
        }
    }
}
