package com.challenge.accountbalanceservice.resources.repositories

import com.challenge.accountbalanceservice.resources.repositories.tables.AccountTable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountJpaRepository : JpaRepository<AccountTable, String>
