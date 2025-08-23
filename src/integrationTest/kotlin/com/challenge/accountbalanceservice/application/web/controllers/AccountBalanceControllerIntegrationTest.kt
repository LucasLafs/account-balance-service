package com.challenge.accountbalanceservice.application.web.controllers

import com.challenge.accountbalanceservice.resources.repositories.AccountJpaRepository
import com.challenge.accountbalanceservice.testing.BaseIntegrationTest
import com.challenge.accountbalanceservice.testing.factory.AccountTableFactory
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class AccountBalanceControllerIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var accountJpaRepository: AccountJpaRepository

    @Test
    fun `given a valid account id when try get account balance should successfully`() {
        val accountId = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa974"
        val accountTable = AccountTableFactory.buildSimpleFixture()

        accountJpaRepository.save(accountTable)

        Given {
            headers(defaultHeaders)
        } When {
            get("/accounts/$accountId/balance")
        } Then {
            statusCode(HttpStatus.SC_OK)

            body("id", Matchers.equalTo(accountId))
            body("owner", Matchers.equalTo(accountTable.owner))
            body("balance.amount", Matchers.equalTo("10.0".toFloat()))
            body("balance.currency", Matchers.equalTo(accountTable.currency))
            body("updated_at", Matchers.matchesRegex(REGEX_OFFSET_DATETIME))
        }
    }

    @Test
    fun `given a valid account id when try get account balance but account not exists should returns account not found`() {
        val accountId = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa976"

        Given {
            headers(defaultHeaders)
        } When {
            get("/accounts/$accountId/balance")
        } Then {
            statusCode(HttpStatus.SC_NOT_FOUND)

            body("type", Matchers.equalTo("ACCOUNT_NOT_FOUND"))
            body("message", Matchers.equalTo("The given account [$accountId] was not found"))
        }
    }

    companion object {
        private const val REGEX_OFFSET_DATETIME =
            "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,9})?-\\d{2}:\\d{2}\$"
    }
}
