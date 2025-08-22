package com.challenge.accountbalanceservice.application.queue.listener

import com.challenge.accountbalanceservice.application.queue.listener.messages.FinancialTransactionDto
import com.challenge.accountbalanceservice.application.queue.listener.messages.toDomain
import com.challenge.accountbalanceservice.domain.services.FinancialTransactionProcessService
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.time.Duration
import kotlin.system.measureTimeMillis

@Component
class FinancialTransactionProcessListener(
    private val financialTransactionProcessService: FinancialTransactionProcessService,
    private val objectMapper: ObjectMapper,
    private val meterRegistry: MeterRegistry
) {
    private val logger = LoggerFactory.getLogger(FinancialTransactionProcessListener::class.java)

    @SqsListener(value = [QUEUE_NAME], maxConcurrentMessages = "500")
    @Suppress("TooGenericExceptionCaught")
    fun execute(@Payload messages: List<String>) {
        logger.info("batch of ${messages.size} financial transaction messages received. messages[$messages]")

        val batchTimer = meterRegistry.timer(TIMER_METRIC_NAME)
        val successCounter = meterRegistry.counter(COUNTER_METRIC_SUCCESS_NAME, "queue", QUEUE_NAME)
        val failureCounter = meterRegistry.counter(COUNTER_METRIC_FAILURE_NAME, "queue", QUEUE_NAME)

        val elapsedMillis = measureTimeMillis {
            try {
                logger.info("initializing financial transaction batch process")
                val financialTransactions = messages.map { message ->
                    objectMapper.readValue(
                        message,
                        FinancialTransactionDto::class.java
                    ).toDomain()
                }

                financialTransactionProcessService.process(financialTransactions)
                successCounter.increment(messages.size.toDouble())

                logger.info("finished processing batch of ${messages.size} messages")
            } catch (ex: Exception) {
                failureCounter.increment(messages.size.toDouble())
                logger.error("failed to process batch of messages", ex)

                throw ex
            }
        }

        batchTimer.record(Duration.ofMillis(elapsedMillis))
    }

    companion object {
        private const val TIMER_METRIC_NAME = "financial.transaction.batch.processing.time"
        private const val COUNTER_METRIC_SUCCESS_NAME = "financial.transaction.messages.success"
        private const val COUNTER_METRIC_FAILURE_NAME = "financial.transaction.messages.failure"
        private const val QUEUE_NAME = "transacoes-financeiras-processadas"
    }
}
