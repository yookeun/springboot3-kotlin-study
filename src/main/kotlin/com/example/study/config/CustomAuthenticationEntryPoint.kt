package com.example.study.config

import com.example.study.exception.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException) {

        ErrorResponse(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.name).exceptionCall(response)
    }
}