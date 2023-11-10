package com.example.study.member.dto

data class LoginDto(
    val userId: String,
    val name: String,
    val password: String,
    val accessToken: String
)