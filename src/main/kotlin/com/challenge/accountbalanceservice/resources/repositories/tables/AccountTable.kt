package com.challenge.accountbalanceservice.resources.repositories.tables

import com.challenge.accountbalanceservice.domain.entities.AccountBalance
import com.challenge.accountbalanceservice.domain.entities.AccountStatus
import com.challenge.accountbalanceservice.domain.entities.Balance
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.domain.Persistable
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.Currency

@Entity(name = "Accounts")
@Table(name = "accounts")
data class AccountTable(
    @Id
    val accountId: String,
    val owner: String,

    @Enumerated(EnumType.STRING)
    val status: AccountStatus,
    val amount: BigDecimal,
    val currency: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,

    @Transient
    val new: Boolean
) : Persistable<String> {
    override fun isNew() = new
    override fun getId() = accountId
}

fun AccountBalance.toTable(isNew: Boolean = true): AccountTable {
    return AccountTable(
        accountId = id,
        owner = owner,
        status = status,
        amount = balance.amount,
        currency = balance.currency.toString(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        new = isNew
    )
}

fun AccountTable.toEntity(): AccountBalance {
    return AccountBalance(
        id = accountId,
        owner = owner,
        status = status,
        balance = Balance(
            amount = amount,
            currency = Currency.getInstance(currency)
        ),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
