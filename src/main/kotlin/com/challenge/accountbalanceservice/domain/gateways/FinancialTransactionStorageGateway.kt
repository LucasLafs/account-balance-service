package com.challenge.accountbalanceservice.domain.gateways

import com.challenge.accountbalanceservice.domain.entities.FinancialTransaction

interface FinancialTransactionStorageGateway {
    fun upsertAll(financialTransactions: List<FinancialTransaction>)
}
