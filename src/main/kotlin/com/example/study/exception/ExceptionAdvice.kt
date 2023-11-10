package com.example.study.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.security.auth.message.AuthException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLException

@RestControllerAdvice
class ExceptionAdvice {

    private val logger = KotlinLogging.logger{}

    @ExceptionHandler(SQLException::class)
    fun sqlException(e: SQLException): ResponseEntity<ErrorResponse> {
        logger.error { "sqlException = ${e.message}" }
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        logger.error { "illegalArgumentException = ${e.message}" }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(HttpStatus.BAD_REQUEST, e.message?:""))
    }

    @ExceptionHandler(RuntimeException::class)
    fun runTimeException(e: RuntimeException): ResponseEntity<ErrorResponse> {
        logger.error { "runTimeException = ${e.message}" }
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.warn { "methodArgumentNotValidException = ${e.message}" }
        val bindingResult = e.bindingResult
        val stringBuilder = StringBuilder()
        for (fieldError in bindingResult.fieldErrors) {
            stringBuilder.append("[")
            stringBuilder.append(fieldError.field)
            stringBuilder.append("=").append(fieldError.defaultMessage)
            stringBuilder.append("]")
        }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, stringBuilder.toString()))

    }

    @ExceptionHandler(AuthException::class)
    fun authException(e: AuthException): ResponseEntity<ErrorResponse> {
        logger.error { "authException = ${e.message}" }
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(HttpStatus.UNAUTHORIZED, e.message?:""))
    }
}