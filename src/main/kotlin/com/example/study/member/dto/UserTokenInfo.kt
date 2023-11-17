package com.example.study.member.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class UserTokenInfo(

    @JsonProperty("userId")
    var userId: String,

    @JsonProperty("accessToken")
    var accessToken: String,

    @JsonProperty("refreshToken")
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