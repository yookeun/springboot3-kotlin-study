package com.example.study.member.service

import com.example.study.handler.JwtTokenHandler
import com.example.study.handler.RedisTokenHandler
import com.example.study.member.domain.Member
import com.example.study.member.dto.MemberDto
import com.example.study.member.dto.MemberRequestDto
import com.example.study.member.dto.toDto
import com.example.study.member.repository.MemberRepository
import io.github.oshai.kotlinlogging.KotlinLogging
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

        val member = memberRepository.save(memberRequestDto.toEntity())
        member.addAuthorities(memberRequestDto.authorities)
        return member.toDto()
    }


}