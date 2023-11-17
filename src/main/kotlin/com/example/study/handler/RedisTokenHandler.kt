package com.example.study.handler

import com.example.study.member.dto.LoginDto
import com.example.study.member.dto.UserTokenInfo
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.querydsl.core.util.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class RedisTokenHandler(
    @Value("\${jwt.refresh-token-expired-days}") var limitDays: Long,
    private val redisTemplate: RedisTemplate<String, Any>) {

    @Throws(JsonProcessingException::class)
    fun findSavedAccessToken(userId: String): Optional<UserTokenInfo> {
        return findByRefreshToken(userId)
    }

    @Throws(JsonProcessingException::class)
    fun saveToken(loginDto: LoginDto) {
        val userTokenInfo: UserTokenInfo = UserTokenInfo(
            userId = loginDto.userId,
            accessToken = loginDto.accessToken,
            refreshToken = loginDto.refreshToken
        )
        saveRedis(userTokenInfo)
    }


    @Throws(JsonProcessingException::class)
    private fun findByRefreshToken(userId: String): Optional<UserTokenInfo> {
        val valueOperations = redisTemplate.opsForValue()
        val json = valueOperations[userId] as String?
        if (StringUtils.isNullOrEmpty(json)) {
            return Optional.empty<UserTokenInfo>()
        }
        val objectMapper = ObjectMapper()
        return Optional.of(objectMapper.readValue(json, UserTokenInfo::class.java))
    }


    @Throws(JsonProcessingException::class)
    private fun saveRedis(userTokenInfo: UserTokenInfo) {
        val valueOperations = redisTemplate.opsForValue()
        val objectMapper = ObjectMapper()
        valueOperations[userTokenInfo.userId] = objectMapper.writeValueAsString(userTokenInfo)
        redisTemplate.expire(userTokenInfo.userId, limitDays, TimeUnit.DAYS)
    }

    @Throws(JsonProcessingException::class)
    fun updateRedis(userTokenInfo: UserTokenInfo) {
        val valueOperations = redisTemplate.opsForValue()
        val objectMapper = ObjectMapper()
        valueOperations[userTokenInfo.userId] = objectMapper.writeValueAsString(userTokenInfo)
    }

    fun deleteRedis(key: String) {
        val valueOperations = redisTemplate.opsForValue()
        valueOperations.getAndDelete(key)
    }
}