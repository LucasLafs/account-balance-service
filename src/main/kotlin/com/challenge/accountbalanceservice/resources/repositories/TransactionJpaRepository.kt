package com.challenge.accountbalanceservice.resources.repositories

import com.challenge.accountbalanceservice.resources.repositories.tables.TransactionTable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionJpaRepository : JpaRepository<TransactionTable, String>
