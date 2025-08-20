package com.challenge.accountbalanceservice.domain.gateways

import com.challenge.accountbalanceservice.domain.entities.AccountBalance

interface AccountStorageGateway {
    fun insert(accountBalance: AccountBalance)
    fun update(accountBalance: AccountBalance)
    fun findById(id: String): AccountBalance?
}
