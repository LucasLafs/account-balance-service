package com.challenge.accountbalanceservice.domain.gateways

import com.challenge.accountbalanceservice.domain.entities.FinancialTransaction

interface FinancialTransactionStorageGateway {
    fun insertOrUpdate(financialTransactions: List<FinancialTransaction>)
}
