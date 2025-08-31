package com.challenge.accountbalanceservice.resources.jpa

import com.challenge.accountbalanceservice.domain.entities.FinancialTransaction
import com.challenge.accountbalanceservice.domain.gateways.FinancialTransactionStorageGateway
import com.challenge.accountbalanceservice.resources.repositories.FinancialTransactionJdbcRepository
import com.challenge.accountbalanceservice.resources.repositories.tables.toTable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FinancialTransactionStorageGatewayJpa(
    private val repository: FinancialTransactionJdbcRepository
) : FinancialTransactionStorageGateway {

    @Transactional
    override fun upsertAll(financialTransactions: List<FinancialTransaction>) {
        val accountTables = financialTransactions.map { it.account.toTable() }
        val transactionTables = financialTransactions.map { it.transaction.toTable() }

        repository.upsertAll(accountTables, transactionTables)
    }
}
