package com.challenge.accountbalanceservice.resources.jpa

import com.challenge.accountbalanceservice.domain.entities.AccountBalance
import com.challenge.accountbalanceservice.domain.gateways.AccountStorageGateway
import com.challenge.accountbalanceservice.resources.repositories.AccountJpaRepository
import com.challenge.accountbalanceservice.resources.repositories.tables.toEntity
import com.challenge.accountbalanceservice.resources.repositories.tables.toTable
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Component
@Transactional(readOnly = true)
class AccountStorageGatewayJpa(
    private val repository: AccountJpaRepository
) : AccountStorageGateway {

    @CircuitBreaker(name = "resource-jpa-cb")
    @Transactional
    override fun insert(accountBalance: AccountBalance) {
        repository.save(accountBalance.toTable(isNew = true))
    }

    @CircuitBreaker(name = "resource-jpa-cb")
    @Transactional
    override fun update(accountBalance: AccountBalance) {
        repository.save(accountBalance.toTable(isNew = false))
    }

    @CircuitBreaker(name = "resource-jpa-cb")
    override fun findById(id: String): AccountBalance? {
        return repository.findById(id).getOrNull()?.toEntity()
    }
}
