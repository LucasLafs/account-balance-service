package com.challenge.accountbalanceservice.resources.jpa

import com.challenge.accountbalanceservice.domain.entities.Transaction
import com.challenge.accountbalanceservice.domain.gateways.TransactionStorageGateway
import com.challenge.accountbalanceservice.resources.repositories.TransactionJpaRepository
import com.challenge.accountbalanceservice.resources.repositories.tables.toTable
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class TransactionStorageGatewayJpa(
    private val repository: TransactionJpaRepository
) : TransactionStorageGateway {

    @CircuitBreaker(name = "resource-jpa-cb")
    @Transactional
    override fun insert(transaction: Transaction) {
        repository.save(transaction.toTable(isNew = true))
    }
}
