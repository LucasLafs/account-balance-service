package com.challenge.accountbalanceservice.application.web.controllers

import com.challenge.accountbalanceservice.application.web.api.AccountBalanceApi
import com.challenge.accountbalanceservice.application.web.dto.AccountBalanceResponseDto
import com.challenge.accountbalanceservice.application.web.extensions.toDto
import com.challenge.accountbalanceservice.domain.services.AccountBalanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountBalanceController(
    private val accountBalanceService: AccountBalanceService
) : AccountBalanceApi {
    override fun getAccountBalance(accountId: String): ResponseEntity<AccountBalanceResponseDto> {
        return ResponseEntity.ok(
            accountBalanceService.getAccountBalanceById(accountId).toDto()
        )
    }
}
