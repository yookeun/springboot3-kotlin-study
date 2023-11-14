package com.example.study.member.controller

import com.example.study.member.dto.MemberDto
import com.example.study.member.dto.MemberSearchCondition
import com.example.study.member.service.MemberService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member")
class MemberController(private val memberService: MemberService) {

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    fun getAllMembers(condition: MemberSearchCondition, pageable: Pageable): Page<MemberDto> {
        return memberService.getAllMembers(condition, pageable)
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun getMember(@PathVariable("id") id: Long): ResponseEntity<MemberDto> {
        return ResponseEntity.ok(memberService.getMember(id))
    }
}