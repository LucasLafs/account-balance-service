package com.challenge.accountbalanceservice.application.exception.handlers

import com.challenge.accountbalanceservice.application.web.dto.BaseErrorResponseDto
import com.challenge.accountbalanceservice.domain.exceptions.AccountNotFoundException
import com.challenge.accountbalanceservice.domain.exceptions.BaseException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class DomainExceptionHandler {
    private val logger = LoggerFactory.getLogger(DomainExceptionHandler::class.java)

    @ExceptionHandler(value = [BaseException::class])
    fun onBaseException(baseException: BaseException): ResponseEntity<BaseErrorResponseDto> {
        logger.error("Exception Handled ${baseException.cause}")
        return ResponseEntity.status(mapStatus(baseException))
            .body(
                BaseErrorResponseDto()
                    .type(baseException.type)
                    .message(baseException.message)
            )
    }

    private fun mapStatus(ex: BaseException): HttpStatus {
        return statusTable[ex.javaClass.simpleName] ?: HttpStatus.INTERNAL_SERVER_ERROR
    }

    companion object {
        private val statusTable: Map<String, HttpStatus> = mapOf(
            BaseException::class.simpleName!! to HttpStatus.UNPROCESSABLE_ENTITY,
            AccountNotFoundException::class.simpleName!! to HttpStatus.NOT_FOUND
        )
    }
}
