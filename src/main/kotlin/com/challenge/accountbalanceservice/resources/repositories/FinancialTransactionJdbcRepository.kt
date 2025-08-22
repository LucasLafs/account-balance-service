package com.challenge.accountbalanceservice.resources.repositories

import com.challenge.accountbalanceservice.resources.repositories.tables.AccountTable
import com.challenge.accountbalanceservice.resources.repositories.tables.TransactionTable
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Statement.SUCCESS_NO_INFO

@Repository
class FinancialTransactionJdbcRepository(
    private val meterRegistry: MeterRegistry,
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {
    fun insertOrUpdate(
        accountTables: List<AccountTable>,
        transactionTables: List<TransactionTable>
    ) {
        val uniqueAccountTables = accountTables
            .groupBy { it.id }
            .map { (_, accounts) ->
                accounts.maxByOrNull { it.updatedAt }!!
            }

        jdbcTemplate.batchUpdate(
            ACCOUNT_SQL_INSERT,
            uniqueAccountTables.map(::mapOfAccountInsertsSqlParameterSource).toTypedArray()
        ).let { result ->
            val success = result.filter { it == SUCCESS_NO_INFO }
            val failure = result.filter { it != SUCCESS_NO_INFO && it != 0 }

            meterRegistry.counter(ACCOUNT_METRIC_SUCCESS_NAME).increment(success.size.toDouble())
            meterRegistry.counter(ACCOUNT_METRIC_FAILURE_NAME).increment(failure.size.toDouble())
        }

        jdbcTemplate.batchUpdate(
            TRANSACTION_SQL_INSERT,
            transactionTables.map(::mapOfTransactionInsertsSqlParameterSource).toTypedArray()
        ).let { result ->
            val success = result.filter { it == SUCCESS_NO_INFO }
            val failure = result.filter { it != SUCCESS_NO_INFO && it != 0 }

            meterRegistry.counter(TRANSACTION_METRIC_SUCCESS_NAME).increment(success.size.toDouble())
            meterRegistry.counter(TRANSACTION_METRIC_FAILURE_NAME).increment(failure.size.toDouble())
        }
    }

    private fun mapOfAccountInsertsSqlParameterSource(row: AccountTable) = MapSqlParameterSource().apply {
        addValue("account_id", row.id)
        addValue("owner", row.owner)
        addValue("status", row.status.name)
        addValue("amount", row.amount)
        addValue("currency", row.currency)
        addValue("created_at", row.createdAt)
        addValue("updated_at", row.updatedAt)
    }

    private fun mapOfTransactionInsertsSqlParameterSource(row: TransactionTable) =
        MapSqlParameterSource().apply {
            addValue("transaction_id", row.transactionId)
            addValue("account_id", row.accountId)
            addValue("type", row.type.name)
            addValue("status", row.status.name)
            addValue("amount", row.amount)
            addValue("currency", row.currency)
            addValue("created_at", row.createdAt)
        }

    companion object {
        private const val ACCOUNT_METRIC_SUCCESS_NAME = "insertOrUpdate.account.affected_rows.count"
        private const val ACCOUNT_METRIC_FAILURE_NAME = "insertOrUpdate.account.failure.count"

        private const val TRANSACTION_METRIC_SUCCESS_NAME = "insertOrUpdate.transactions.affected_rows.count"
        private const val TRANSACTION_METRIC_FAILURE_NAME = "insertOrUpdate.transactions.failure.count"

        private const val ACCOUNT_SQL_INSERT = """
            INSERT INTO accounts (
                account_id, owner, status, amount, currency, created_at, updated_at
            ) VALUES (:account_id, :owner, :status, :amount, :currency, :created_at, :updated_at)
            ON CONFLICT (account_id) DO UPDATE 
            SET amount = EXCLUDED.amount,
                status = EXCLUDED.status,
                updated_at = EXCLUDED.updated_at
        """

        private const val TRANSACTION_SQL_INSERT = """
            INSERT INTO transactions (
                transaction_id, account_id, type, status, amount, currency, created_at
            ) VALUES (:transaction_id, :account_id, :type, :status, :amount, :currency, :created_at)
            ON CONFLICT (transaction_id, account_id) DO NOTHING
        """
    }
}
