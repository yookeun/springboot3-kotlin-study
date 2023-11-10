package com.example.study.config

import com.example.study.member.domain.Member
import com.example.study.member.enums.Authority
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors

class User(member: Member): UserDetails {

    private var username: String = member.userId
    private var password: String = member.password

    private val authorities: List<String> = Arrays.stream(Authority.values()).map(Authority::name).toList()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities.stream().map { role -> SimpleGrantedAuthority(role) }.collect(
            Collectors.toSet())
    }

    override fun getPassword(): String {
        return username
    }

    override fun getUsername(): String {
        return password
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return false
    }
}