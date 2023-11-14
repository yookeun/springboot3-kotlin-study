package com.example.study.member.service

import com.example.study.handler.JwtTokenHandler
import com.example.study.handler.RedisTokenHandler
import com.example.study.member.domain.Member
import com.example.study.member.dto.LoginDto
import com.example.study.member.dto.LoginRequestDto
import com.example.study.member.dto.MemberDto
import com.example.study.member.dto.MemberRequestDto
import com.example.study.member.dto.MemberSearchCondition
import com.example.study.member.dto.toDto
import com.example.study.member.repository.MemberRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

    private fun isMatchPassword(rawPassword: String, dbPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, dbPassword)
    }


}