package com.example.study.member.domain

import com.example.study.common.BaseEntity
import com.example.study.member.enums.Authority
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "MEMBER_AUTHORITY")
class MemberAuthority(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_AUTHORITY_ID")
    var id: Long? = null,

    @Column(name = "AUTHORITY", columnDefinition = "varchar(20) not null")
    @Enumerated(EnumType.STRING)
    var authority: Authority,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    var member: Member? = null

): BaseEntity()
