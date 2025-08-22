package com.challenge.accountbalanceservice.domain.gateways

import com.challenge.accountbalanceservice.domain.entities.AccountBalance

interface AccountStorageGateway {
    fun findById(id: String): AccountBalance?
}
