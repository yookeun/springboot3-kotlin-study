package com.example.study.member.service

import com.example.study.member.dto.MemberDto
import com.example.study.member.dto.MemberRequestDto
import com.example.study.member.dto.fromEntity
import com.example.study.member.repository.MemberRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class MemberService(private val memberRepository: MemberRepository) {

    private val logger = KotlinLogging.logger{}

    @Transactional
    fun saveMember(memberRequestDto: MemberRequestDto): MemberDto  {
        logger.atInfo { "memberDto save ${memberRequestDto}" }
        val member = memberRepository.save(memberRequestDto.toEntity())
        member.addAuthorities(memberRequestDto.authorities)
        return member.fromEntity()
    }
}