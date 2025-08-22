package com.challenge.accountbalanceservice.application.queue.listener

import com.challenge.accountbalanceservice.application.queue.listener.messages.FinancialTransactionDto
import com.challenge.accountbalanceservice.domain.entities.FinancialTransaction
import com.challenge.accountbalanceservice.domain.services.FinancialTransactionProcessService
import com.challenge.accountbalanceservice.factories.queue.listener.messages.FinancialTransactionDtoFactory
import com.fasterxml.jackson.databind.ObjectMapper
import io.micrometer.core.instrument.MeterRegistry
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class FinancialTransactionProcessListenerTest {
    private val service = mockk<FinancialTransactionProcessService>()
    private val objectMapper = mockk<ObjectMapper>()
    private val meterRegistry = mockk<MeterRegistry>(relaxed = true)

    private val listener = FinancialTransactionProcessListener(
        financialTransactionProcessService = service,
        objectMapper = objectMapper,
        meterRegistry = meterRegistry
    )

    @Test
    fun `should call successfully financial transaction process`() {
        val messageBody = FinancialTransactionDtoFactory.buildSimpleFixture()
        val slot = slot<List<FinancialTransaction>>()

        every {
            objectMapper.readValue(messageBody.toString(), FinancialTransactionDto::class.java)
        } returns messageBody

        every { service.process(capture(slot)) } just runs

        assertDoesNotThrow { listener.execute(listOf(messageBody.toString())) }

        val slotCaptured = slot.captured.single()
        assertEquals(messageBody.transaction.id, slotCaptured.transaction.transactionId)
        assertEquals(messageBody.transaction.status, slotCaptured.transaction.status)
        assertEquals(messageBody.account.id, slotCaptured.account.accountId)
        assertEquals(messageBody.account.balance.amount, slotCaptured.account.balance.amount)
        assertEquals(messageBody.account.balance.currency, slotCaptured.account.balance.currency.currencyCode)

        verify(exactly = 1) { service.process(any()) }
    }

    @Test
    fun `should throws exception when service returns an error`() {
        val messageBody = FinancialTransactionDtoFactory.buildSimpleFixture()

        every {
            objectMapper.readValue(messageBody.toString(), FinancialTransactionDto::class.java)
        } returns messageBody

        every { service.process(any()) } throws RuntimeException()

        assertThrows<RuntimeException> { listener.execute(listOf(messageBody.toString())) }

        verify(exactly = 1) { service.process(any()) }
    }

    @Test
    fun `should throws exception when message parser returns an error`() {
        val messageBody = FinancialTransactionDtoFactory.buildSimpleFixture()

        every {
            objectMapper.readValue(messageBody.toString(), FinancialTransactionDto::class.java)
        } throws RuntimeException()

        assertThrows<RuntimeException> { listener.execute(listOf(messageBody.toString())) }

        verify(exactly = 0) { service.process(any()) }
    }
}
