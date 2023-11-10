package com.example.study.handler

import com.example.study.member.domain.Member
import com.example.study.member.domain.MemberAuthority
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import javax.crypto.spec.SecretKeySpec

@Component
class JwtTokenHandler(@Value("\${jwt.secret-key}") var jwtSecretKey: String,
                      @Value("\${jwt.access-token-expired-min}") var accessTokenExpiredMin: Long,
                      @Value("\${jwt.refresh-token-expired-days}") var refreshTokenExpiredDays: Long) {
    private fun createToken(claims: Map<String, Any?>): String {
        val secretKeyEncodeBase64 = Encoders.BASE64.encode(jwtSecretKey.toByteArray())
        val keyBytes = Decoders.BASE64.decode(secretKeyEncodeBase64)
        val key: Key = Keys.hmacShaKeyFor(keyBytes)
        return Jwts.builder()
            .signWith(key)
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * accessTokenExpiredMin))
            .compact()
    }

    private fun createRefreshToken(claims: Map<String, Any?>): String {
        val secretKeyEncodeBase64 = Encoders.BASE64.encode(jwtSecretKey.toByteArray())
        val keyBytes = Decoders.BASE64.decode(secretKeyEncodeBase64)
        val key: Key = Keys.hmacShaKeyFor(keyBytes)
        return Jwts.builder()
            .signWith(key)
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * refreshTokenExpiredDays))
            .compact()
    }

    fun extractAllClaims(token: String): JwtResult {
        val sa = SignatureAlgorithm.HS256
        val secretKeySpec = SecretKeySpec(jwtSecretKey.toByteArray(), sa.jcaName)
        val claims: Claims
        val jwtResult = JwtResult()
        try {
            claims = Jwts.parserBuilder().setSigningKey(secretKeySpec).build()
                .parseClaimsJws(token).body
            jwtResult.jwtResultType = JwtResultType.TOKEN_SUCCESS
            jwtResult.claims = claims
        } catch (e: ExpiredJwtException) {
            jwtResult.jwtResultType = JwtResultType.TOKEN_EXPIRED
            jwtResult.claims = null
        } catch (e: JwtException) {
            jwtResult.jwtResultType = JwtResultType.TOKEN_INVALID
            jwtResult.claims = null
        }
        return jwtResult
    }

    fun generateToken(member: Member): String {
        val claims: MutableMap<String, Any?> = HashMap()
        claims["username"] = member.userId
        val auths = member.memberAuthorityList.stream()
            .map { memberAuthority: MemberAuthority -> memberAuthority.authority.name }
            .collect(Collectors.joining(","))
        claims["authorities"] = auths
        return createToken(claims)
    }

    fun generateRefreshToken(member: Member): String {
        val claims: MutableMap<String, Any?> = HashMap()
        claims["username"] = member.userId
        return createRefreshToken(claims)
    }


}