package com.example.study.member.controller

import com.example.study.member.dto.MemberDto
import com.example.study.member.dto.MemberRequestDto
import com.example.study.member.service.MemberService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class LoginController(private val memberService: MemberService) {

    private val logger = KotlinLogging.logger{}

    @GetMapping
    fun hello(): String {
        return "hello"
    }

    @PostMapping
    fun create(@RequestBody memberRequestDto: MemberRequestDto): ResponseEntity<MemberDto> {
        return ResponseEntity.ok(memberService.saveMember(memberRequestDto))
    }
}