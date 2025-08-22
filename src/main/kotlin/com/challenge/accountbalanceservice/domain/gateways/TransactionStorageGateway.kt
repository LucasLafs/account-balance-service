package com.challenge.accountbalanceservice.domain.gateways

import com.challenge.accountbalanceservice.domain.entities.Transaction

interface TransactionStorageGateway {
    fun findByAccountId(accountId: String): List<Transaction>
}
