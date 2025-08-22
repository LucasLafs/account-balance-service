package com.challenge.accountbalanceservice.testing

import com.challenge.accountbalanceservice.testing.extensions.postgres.PostgresEmbeddedExtension
import com.challenge.accountbalanceservice.testing.extensions.sqs.SQSEmbeddedConfiguration
import com.github.tomakehurst.wiremock.client.WireMock
import io.awspring.cloud.sqs.operations.SqsTemplate
import io.restassured.RestAssured
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import software.amazon.awssdk.services.sqs.SqsAsyncClient

@ActiveProfiles("integration-test")
@ExtendWith(
    PostgresEmbeddedExtension::class
)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [SQSEmbeddedConfiguration::class])
@AutoConfigureWireMock
abstract class BaseIntegrationTest {

    @LocalServerPort
    private var serverPort = -1

    @Autowired
    lateinit var sqsAsyncClient: SqsAsyncClient

    @Autowired
    protected lateinit var sqsTemplate: SqsTemplate

    protected val queueName = "transacoes-financeiras-processadas"

    @BeforeEach
    fun setupQueue() {
        resetAllRequests()

        val queueUrl = sqsAsyncClient.createQueue { it.queueName(queueName) }
            .join()
            .queueUrl()

        LOG.info("Queue created: $queueUrl")
    }

    protected fun sendTestMessage(messages: String) {
        sqsTemplate.send(queueName, messages)
    }

    protected val defaultHeaders
        get() = HttpHeaders().apply {
            add("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            add("Authorization", "shared-secret")
        }

    protected fun resetAllRequests() {
        WireMock.resetAllRequests()
        if (serverPort > 0) {
            RestAssured.port = serverPort
            LOG.info("rest-assured ready requesting on port $serverPort")
        }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            SQSEmbeddedConfiguration.start()
        }

        @JvmStatic
        @AfterAll
        fun teardown() {
            SQSEmbeddedConfiguration.stop()
        }

        private val LOG = LoggerFactory.getLogger(BaseIntegrationTest::class.java)
    }
}
