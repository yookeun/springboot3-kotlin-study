package com.example.study.member.repository

import com.example.study.member.domain.Member
import com.example.study.member.dto.MemberSearchCondition
import org.springframework.data.domain.Page

import org.springframework.data.domain.Pageable


interface MemberRepositoryCustom {
    fun getAllMembers(condition: MemberSearchCondition, pageable: Pageable): Page<Member>
}