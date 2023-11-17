package com.example.study.member.service

import com.example.study.exception.AuthException
import com.example.study.handler.JwtResult
import com.example.study.handler.JwtResultType
import com.example.study.handler.JwtTokenHandler
import com.example.study.handler.RedisTokenHandler
import com.example.study.member.domain.Member
import com.example.study.member.dto.LoginDto
import com.example.study.member.dto.LoginRequestDto
import com.example.study.member.dto.MemberDto
import com.example.study.member.dto.MemberRequestDto
import com.example.study.member.dto.MemberSearchCondition
import com.example.study.member.dto.MemberUpdateDto
import com.example.study.member.dto.RefreshTokenDto
import com.example.study.member.dto.UserTokenInfo
import com.example.study.member.dto.toDto
import com.example.study.member.repository.MemberRepository
import com.fasterxml.jackson.core.JsonProcessingException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils
import java.util.*


@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val jwtTokenHandler: JwtTokenHandler,
    private val passwordEncoder: PasswordEncoder,
    private val redisTokenHandler: RedisTokenHandler
    ) {

    private val logger = KotlinLogging.logger{}

    @Transactional
    fun saveMember(memberRequestDto: MemberRequestDto): MemberDto  {
        logger.atInfo { message = "saveMember = ${memberRequestDto}" }
        val optionalMember: Optional<Member> = memberRepository.findByUserId(memberRequestDto.userId);
        if (optionalMember.isPresent) {
            throw IllegalArgumentException("This userId is already in use.")
        }
        memberRequestDto.password = passwordEncoder.encode(memberRequestDto.password)

        val member = memberRepository.save(memberRequestDto.toEntity())
        member.addAuthorities(memberRequestDto.authorities)
        return member.toDto()
    }

    fun loginProcess(loginRequestDto: LoginRequestDto): LoginDto {
        val optionalMember: Optional<Member> = memberRepository.findByUserId(loginRequestDto.userId);
        if (optionalMember.isEmpty || !isMatchPassword(loginRequestDto.password,
                optionalMember.get().password)) {
            throw IllegalArgumentException("The ID or password is incorrect.")
        }
        val loginDto = LoginDto(
            userId = optionalMember.get().userId,
            password = optionalMember.get().password,
            name = optionalMember.get().name,
            accessToken = jwtTokenHandler.generateToken(optionalMember.get()),
            refreshToken = jwtTokenHandler.generateRefreshToken(optionalMember.get())
        )

        //Save to redis
        try {
            redisTokenHandler.saveToken(loginDto)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
        return loginDto
    }

    fun getMember(id: Long): MemberDto {
        val member = memberRepository.findById(id)
            .orElseThrow { throw IllegalArgumentException("This ID does not exist.") }
        return member.toDto()
    }

    fun getAllMembers(condition: MemberSearchCondition, pageable: Pageable): Page<MemberDto> {
        return memberRepository.getAllMembers(condition, pageable).map { member -> member.toDto() }
    }

    @Transactional
    fun updateMember(id: Long, memberUpdateDto: MemberUpdateDto): MemberDto {
        val member = memberRepository.findById(id)
            .orElseThrow { throw IllegalArgumentException("This ID does not exist.") }
        member.name = memberUpdateDto.name
        member.gender = memberUpdateDto.gender
        member.password = passwordEncoder.encode(memberUpdateDto.password)
        if (!CollectionUtils.isEmpty(memberUpdateDto.authorities)) {
            member.updateAuthorities(memberUpdateDto.authorities)
        }
        return member.toDto()
    }


    fun getRenewAccessToken(refreshTokenDto: RefreshTokenDto): String {
        val userTokenInfo: UserTokenInfo = checkToken(refreshTokenDto)
        //Create a new access token.
        val member = memberRepository.findByUserId(userTokenInfo.userId)
            .orElseThrow { AuthException(JwtResultType.UNUSUAL_REQUEST.name) }
        val renewAccessToken = jwtTokenHandler.generateToken(member)
        userTokenInfo.accessToken = renewAccessToken

        try {
            redisTokenHandler.updateRedis(userTokenInfo)
        } catch (e: JsonProcessingException) {
            logger.error { e }
            throw RuntimeException(e)
        }
        return renewAccessToken
    }


    private fun checkToken(refreshTokenDto: RefreshTokenDto): UserTokenInfo {
        var jwtResult: JwtResult
        val optionalUserTokenInfo: Optional<UserTokenInfo>
        try {
            optionalUserTokenInfo = redisTokenHandler.findSavedAccessToken(refreshTokenDto.userId)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }

        //1. Throw an error if userId has no stored key value
        if (optionalUserTokenInfo.isEmpty) {
            logger.warn { "Not found Redis key = ${refreshTokenDto.userId}" }
            throw AuthException(JwtResultType.TOKEN_EXPIRED.name)
        }

        //2.If the received refreshToken is different from the stored refreshToken, a 401 error is thrown.
        val userTokenInfo = optionalUserTokenInfo.get()
        if (refreshTokenDto.refreshToken != userTokenInfo.refreshToken) {
            logger.warn { "requested refreshToken != saved refreshToken" }
            throw AuthException(JwtResultType.UNUSUAL_REQUEST.name)
        }

        //3. If the refreshToken is an invalid or expired token, delete the key and throw a 401 error.
        jwtResult = jwtTokenHandler.extractAllClaims(userTokenInfo.refreshToken)
        if (jwtResult.jwtResultType != JwtResultType.TOKEN_SUCCESS) {
            logger.warn { "refreshToken invalid or expired: ${jwtResult.jwtResultType.name}" }
            throw AuthException(jwtResult.jwtResultType.name)
        }

        //4.If the received accessToken is different from the stored accessToken, delete the key and handle a 401 error.
        if (refreshTokenDto.accessToken != userTokenInfo.accessToken) {
            redisTokenHandler.deleteRedis(userTokenInfo.userId)
            logger.warn { "requested accessToken != saved accessToken" }
            throw AuthException(JwtResultType.UNUSUAL_REQUEST.name)
        }

        //5.If the existing accessToken is not yet an expired token, delete the key as an abnormal request and throw a 401 error.
        jwtResult = jwtTokenHandler.extractAllClaims(userTokenInfo.refreshToken)
        if (jwtResult.jwtResultType == JwtResultType.TOKEN_SUCCESS) {
            redisTokenHandler.deleteRedis(userTokenInfo.userId)
            logger.warn { "accessToken has not expired yet : ${jwtResult.jwtResultType.name}"}
            throw AuthException(JwtResultType.UNUSUAL_REQUEST.name)
        }
        return userTokenInfo
    }


    private fun isMatchPassword(rawPassword: String, dbPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, dbPassword)
    }

}