package com.example.study.handler

import io.jsonwebtoken.Claims

class JwtResult {
    lateinit var jwtResultType: JwtResultType
    var claims: Claims? = null
}

enum class JwtResultType {
    TOKEN_EXPIRED,
    TOKEN_INVALID,
    TOKEN_SUCCESS,
    UNUSUAL_REQUEST
}