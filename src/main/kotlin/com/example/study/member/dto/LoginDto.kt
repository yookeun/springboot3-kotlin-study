package com.example.study.member.dto

import jakarta.validation.constraints.NotBlank

data class LoginDto(
    val userId: String,
    val name: String,
    val password: String,
    val accessToken: String,
    val refreshToken: String
)

data class LoginRequestDto(

    @field:NotBlank(message = "required")
    val userId: String,

    @field:NotBlank(message = "required")
    val password: String
)