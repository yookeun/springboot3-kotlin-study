package com.example.study.member.dto

import jakarta.validation.constraints.NotBlank

data class UserTokenInfo(
    var userId: String,
    var accessToken: String,
    var refreshToken: String
)

data class RefreshTokenDto(
    @field:NotBlank(message = "required")
    val userId: String,

    @field:NotBlank(message = "required")
    val accessToken: String,

    @field:NotBlank(message = "required")
    val refreshToken: String
)