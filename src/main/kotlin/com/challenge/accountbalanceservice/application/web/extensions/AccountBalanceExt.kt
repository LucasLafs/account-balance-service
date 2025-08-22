package com.challenge.accountbalanceservice.application.web.extensions

import com.challenge.accountbalanceservice.application.web.dto.AccountBalanceResponseDto
import com.challenge.accountbalanceservice.application.web.dto.BalanceObjectDto
import com.challenge.accountbalanceservice.domain.entities.AccountBalance
import com.challenge.accountbalanceservice.domain.entities.Balance

fun AccountBalance.toDto(): AccountBalanceResponseDto {
    return AccountBalanceResponseDto(
        accountId,
        owner,
        balance.toDto(),
        updatedAt
    )
}

fun Balance.toDto(): BalanceObjectDto {
    return BalanceObjectDto(
        amount.toDouble(),
        currency.toString()
    )
}
