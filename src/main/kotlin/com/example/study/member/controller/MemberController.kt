package com.example.study.member.controller

import com.example.study.member.service.MemberService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member")
class MemberController(private val memberService: MemberService) {




}