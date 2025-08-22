package com.challenge.accountbalanceservice.domain.services

import com.challenge.accountbalanceservice.domain.entities.AccountBalance
import com.challenge.accountbalanceservice.domain.exceptions.AccountNotFoundException
import com.challenge.accountbalanceservice.domain.gateways.AccountStorageGateway
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RetrieveAccountBalanceService(
    private val accountStorageGateway: AccountStorageGateway
) {
    private val logger = LoggerFactory.getLogger(RetrieveAccountBalanceService::class.java)

    fun getAccountBalanceById(id: String): AccountBalance {
        logger.info("Retrieve the current account [$id] balance")

        return accountStorageGateway.findById(id) ?: throw AccountNotFoundException(
            message = "The given account [$id] was not found"
        )
    }
}
