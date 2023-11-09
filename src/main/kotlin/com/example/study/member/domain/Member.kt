package com.example.study.member.domain

import com.example.study.common.BaseEntity
import com.example.study.member.dto.MemberAuthorityRequestDto
import com.example.study.member.enums.Authority
import com.example.study.member.enums.Gender
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.function.Consumer
import java.util.stream.Collectors

@Entity
@Table(name = "MEMBER")
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    var id: Long? = null,

    @Column(name = "USER_ID", nullable = false, length = 20, unique = true)
    var userId: String,

    @Column(name = "PASSWORD", nullable = false, length = 100)
    var password: String,

    @Column(name = "NAME")
    var name: String,

    @Column(name = "GENDER")
    @Enumerated(value = EnumType.STRING)
    var gender: Gender,

    @Column(name = "PHONE")
    var phone: String,

    @OneToMany(
        mappedBy = "member",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var memberAuthorityList: MutableList<MemberAuthority> = mutableListOf()

): BaseEntity() {

    fun updateName(name:String) {
        this.name = name
    }

    fun updateGender(gender: Gender) {
        this.gender = gender
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updatePhone(phone: String) {
        this.phone = phone
    }

    fun addAuthorities(memberAuthorityRequestDtoList: List<MemberAuthorityRequestDto>) {
        memberAuthorityRequestDtoList.forEach(Consumer {
            memberAuthorityRequestDto: MemberAuthorityRequestDto ->
            memberAuthorityRequestDto.member = this
            memberAuthorityList.add(memberAuthorityRequestDto.toEntity())
        })

    }

    fun getAuthorities(): List<Authority> {
        return memberAuthorityList.stream()
            .map(MemberAuthority::authority).collect(Collectors.toList())
    }


    fun updateAuthorities(memberAuthorityRequestDtoList: List<MemberAuthorityRequestDto>) {
        memberAuthorityList.clear()
        addAuthorities(memberAuthorityRequestDtoList)
    }

}