package com.challenge.accountbalanceservice.application.web.controllers

import com.challenge.accountbalanceservice.application.web.api.AccountBalanceApi
import com.challenge.accountbalanceservice.application.web.dto.AccountBalanceResponseDto
import com.challenge.accountbalanceservice.application.web.extensions.toDto
import com.challenge.accountbalanceservice.domain.services.RetrieveAccountBalanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountBalanceController(
    private val retrieveAccountBalanceService: RetrieveAccountBalanceService
) : AccountBalanceApi {
    override fun getAccountBalance(accountId: String): ResponseEntity<AccountBalanceResponseDto> {
        return ResponseEntity.ok(
            retrieveAccountBalanceService.getAccountBalanceById(accountId).toDto()
        )
    }
}
