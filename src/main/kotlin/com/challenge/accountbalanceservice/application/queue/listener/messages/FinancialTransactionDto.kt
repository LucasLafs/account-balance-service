package com.challenge.accountbalanceservice.application.queue.listener.messages

import com.challenge.accountbalanceservice.application.extensions.microsToOffsetDateTime
import com.challenge.accountbalanceservice.application.extensions.secondsToOffsetDateTime
import com.challenge.accountbalanceservice.domain.entities.AccountBalance
import com.challenge.accountbalanceservice.domain.entities.AccountStatus
import com.challenge.accountbalanceservice.domain.entities.Balance
import com.challenge.accountbalanceservice.domain.entities.FinancialTransaction
import com.challenge.accountbalanceservice.domain.entities.Transaction
import com.challenge.accountbalanceservice.domain.entities.TransactionStatus
import com.challenge.accountbalanceservice.domain.entities.TransactionType
import java.math.BigDecimal
import java.util.Currency

data class FinancialTransactionDto(
    val transaction: TransactionDto,
    val account: AccountDto
)

data class TransactionDto(
    val id: String,
    val type: TransactionType,
    val status: TransactionStatus,
    val amount: BigDecimal,
    val currency: String,
    val timestamp: Long
)

data class AccountDto(
    val id: String,
    val owner: String,
    val createdAt: Long,
    val status: AccountStatus,
    val balance: BalanceDto
)

data class BalanceDto(
    val amount: BigDecimal,
    val currency: String
)

fun FinancialTransactionDto.toDomain(): FinancialTransaction {
    return FinancialTransaction(
        transaction = transaction.toDomain(account.id),
        account = account.toDomain()
    )
}

fun TransactionDto.toDomain(accountId: String): Transaction {
    return Transaction(
        transactionId = id,
        accountId = accountId,
        type = type,
        status = status,
        amount = amount,
        currency = Currency.getInstance(currency),
        createdAt = microsToOffsetDateTime(micros = timestamp)
    )
}

fun AccountDto.toDomain(): AccountBalance {
    return AccountBalance(
        accountId = id,
        owner = owner,
        status = status,
        balance = balance.toDomain(),
        createdAt = secondsToOffsetDateTime(seconds = createdAt)
    )
}

fun BalanceDto.toDomain(): Balance {
    return Balance(
        amount = amount,
        currency = Currency.getInstance(currency)
    )
}
