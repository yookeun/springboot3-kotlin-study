package com.example.study.member.dto

import com.example.study.member.domain.Member
import com.example.study.member.domain.MemberAuthority
import com.example.study.member.enums.Authority
import com.example.study.member.enums.Gender
import jakarta.validation.constraints.NotBlank

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

    @field:NotBlank(message = "required")
    val userId: String,

    @field:NotBlank(message = "required")
    var password: String,

    @field:NotBlank(message = "required")
    val name: String,

    @field:NotBlank(message = "required")
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

data class MemberUpdateDto(

    @field:NotBlank(message = "required")
    var password: String,

    @field:NotBlank(message = "required")
    val name: String,

    @field:NotBlank(message = "required")
    val gender: Gender,

    val phone: String,
    val authorities: List<MemberAuthorityRequestDto>
)

data class MemberAuthorityRequestDto(
    var authority: Authority,
    var member: Member?
) {
    fun toEntity(): MemberAuthority = MemberAuthority(
        authority = authority,
        member = member
    )
}

data class MemberAuthorityDto(
    var authority: Authority
)

data class MemberSearchCondition(
    var searchName: String = "",
    var gender: String = ""
)

fun MemberAuthority.toDto(): MemberAuthorityDto = MemberAuthorityDto(
    authority = authority
)



