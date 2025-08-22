package com.challenge.accountbalanceservice.domain.entities

data class FinancialTransaction(
    val transaction: Transaction,
    val account: AccountBalance
)
