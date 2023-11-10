package com.example.study.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus

class ErrorResponse(var httpStatus: HttpStatus, var message: String) {
    fun exceptionCall(response: HttpServletResponse) {
        val objectMapper = ObjectMapper()
        response.status = httpStatus.value()
        response.characterEncoding = "utf-8"
        response.contentType = "application/json"
        response.writer.write(
            objectMapper.writeValueAsString(ErrorResponse(httpStatus, message)))
    }
}