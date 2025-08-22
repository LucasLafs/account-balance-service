package com.challenge.accountbalanceservice.application.queue.listeners

import com.challenge.accountbalanceservice.domain.gateways.AccountStorageGateway
import com.challenge.accountbalanceservice.domain.gateways.TransactionStorageGateway
import com.challenge.accountbalanceservice.testing.BaseIntegrationTest
import com.challenge.accountbalanceservice.testing.factory.JsonFactory
import com.jayway.jsonpath.JsonPath
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class FinancialTransactionProcessListenerIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var accountStorageGateway: AccountStorageGateway

    @Autowired
    private lateinit var transactionStorageGateway: TransactionStorageGateway

    @Test
    fun `given a valid list of message with financial transactions when receive by listener should insert all messages in database`() {
        val accountId = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975"
        val firstMessage = JsonPath.parse(JsonFactory.jsonMessageRead("financial_transaction_success"))
        val secondMessage = JsonPath.parse(JsonFactory.jsonMessageRead("financial_transaction_success"))
        val thirdMessage = JsonPath.parse(JsonFactory.jsonMessageRead("financial_transaction_success"))

        secondMessage.set("transaction.id", "8e8ae808-b154-48b5-9f3e-553935cc4544")
        thirdMessage.set("transaction.id", "8e8ae808-b154-48b5-9f3e-553935cc4545")

        sendTestMessage(firstMessage.jsonString())
        sendTestMessage(secondMessage.jsonString())
        sendTestMessage(thirdMessage.jsonString())

        await.atMost(Duration.ofSeconds(10)).untilAsserted {
            val accountBalance = accountStorageGateway.findById(accountId)
            val accountTransactions = transactionStorageGateway.findByAccountId(accountId)

            assertNotNull(accountBalance)
            assertNotNull(accountTransactions)
            assertEquals(3, accountTransactions.size)
            assertEquals(accountId, accountBalance.accountId)
            assertEquals(accountId, accountTransactions.last().accountId)
        }
    }

    @Test
    fun `given a list of message with financial transactions when receive by listener and one message is invalid should not insert all messages in database`() {
        val accountId = "5b19c8b6-0cc4-4c72-a989-0c2ee15fa975"
        val firstMessage = JsonPath.parse(JsonFactory.jsonMessageRead("financial_transaction_success"))
        val secondMessage = JsonPath.parse(JsonFactory.jsonMessageRead("financial_transaction_error"))
        val thirdMessage = JsonPath.parse(JsonFactory.jsonMessageRead("financial_transaction_success"))

        thirdMessage.set("transaction.id", "8e8ae808-b154-48b5-9f3e-553935cc4545")

        sendTestMessage(firstMessage.jsonString())
        sendTestMessage(secondMessage.jsonString())
        sendTestMessage(thirdMessage.jsonString())

        await.atMost(Duration.ofSeconds(10)).untilAsserted {
            val accountBalance = accountStorageGateway.findById(accountId)
            val accountTransactions = transactionStorageGateway.findByAccountId(accountId)

            assertNotNull(accountBalance)
            assertNotNull(accountTransactions)
            assertEquals(2, accountTransactions.size)
            assertEquals(accountId, accountBalance.accountId)
            assertEquals(accountId, accountTransactions.last().accountId)
        }
    }
}
