package com.example.study.config

import com.example.study.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserDetailService(private val memberRepository: MemberRepository): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val optionalMember = memberRepository.findByUserId(username)
        if (optionalMember.isEmpty) {
            throw UsernameNotFoundException("UsernameNotFound ${username}")
        }
        return User(optionalMember.get())
    }
}

