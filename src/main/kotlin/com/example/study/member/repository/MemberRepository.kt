package com.example.study.member.repository

import com.example.study.member.domain.Member
import org.springframework.data.repository.Repository
import java.util.*


@org.springframework.stereotype.Repository
interface MemberRepository: Repository<Member, Long> {
    fun save(member: Member): Member
    fun findByUserId(userId: String): Optional<Member>
}