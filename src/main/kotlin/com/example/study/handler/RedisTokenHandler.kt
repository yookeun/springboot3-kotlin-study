package com.example.study.handler

import com.example.study.member.dto.UserTokenInfo
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.querydsl.core.util.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class RedisTokenHandler(
    @Value("\${jwt.refresh-token-expired-days}") limitDays: Long,
    private val redisTemplate: RedisTemplate<String, Any>) {

    @Throws(JsonProcessingException::class)
    fun findSavedAccessToken(userId: String): Optional<UserTokenInfo> {
        return findByRefreshToken(userId)
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
}