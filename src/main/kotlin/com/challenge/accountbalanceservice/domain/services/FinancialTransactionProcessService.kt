package com.challenge.accountbalanceservice.domain.services

import com.challenge.accountbalanceservice.domain.entities.FinancialTransaction
import com.challenge.accountbalanceservice.domain.gateways.FinancialTransactionStorageGateway
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FinancialTransactionProcessService(
    private val financialTransactionStorageGateway: FinancialTransactionStorageGateway
) {
    private val logger = LoggerFactory.getLogger(FinancialTransactionProcessService::class.java)

    @Transactional
    fun process(financialTransactions: List<FinancialTransaction>) {
        logger.info("starting financial transaction process.")

        financialTransactionStorageGateway.insertOrUpdate(financialTransactions)

        logger.info("finishing financial transaction process.")
    }
}
