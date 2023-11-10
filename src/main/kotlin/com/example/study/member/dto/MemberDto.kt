package com.example.study.member.dto

import com.example.study.member.domain.Member
import com.example.study.member.domain.MemberAuthority
import com.example.study.member.enums.Authority
import com.example.study.member.enums.Gender

data class MemberDto(
    val id: Long?,
    val userId: String,
    val password: String,
    val name: String,
    val gender: Gender,
    val phone: String,
    var authorities: List<Authority>
)

fun Member.toDto(): MemberDto {
    return MemberDto(
        id = id,
        userId = userId,
        password = password,
        name = name,
        gender = gender,
        phone = phone,
        authorities = getAuthorities()
    )
}

data class MemberRequestDto(
    val userId: String,
    val password: String,
    val name: String,
    val gender: Gender,
    val phone: String,
    val authorities: List<MemberAuthorityRequestDto>
) {
    fun toEntity() = Member(
        userId = userId,
        password = password,
        name = name,
        gender = gender,
        phone = phone,
    )
}

data class MemberAuthorityRequestDto(
    var authority: Authority,
    var member: Member?
) {
    fun toEntity(): MemberAuthority = MemberAuthority(
        authority = authority,
        member = member
    )
}

data class MemberAuthorityDto (
    var authority: Authority
)

fun MemberAuthority.toDto(memberAuthority: MemberAuthority): MemberAuthorityDto = MemberAuthorityDto(
    authority = memberAuthority.authority
)

