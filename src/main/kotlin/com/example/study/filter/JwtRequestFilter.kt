package com.example.study.filter

import com.example.study.config.UserDetailService
import com.example.study.exception.ErrorResponse
import com.example.study.handler.JwtResult
import com.example.study.handler.JwtResultType
import com.example.study.handler.JwtTokenHandler
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtRequestFilter(
    private val jwtTokenHandler: JwtTokenHandler,
    private val userDetailService: UserDetailService): OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val excludePath = arrayOf("favicon", "/member", "/docs")
        val path = request.requestURI
        return Arrays.stream(excludePath).anyMatch { prefix: String? ->
            path.startsWith(
                prefix!!
            )
        }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        var username = ""
        var token = ""


        //Parse the token attached below the Bearer part of the Header.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7)
        }

        if (!StringUtils.hasText(token)) {
            ErrorResponse(HttpStatus.BAD_REQUEST, JwtResultType.UNUSUAL_REQUEST.name)
                .exceptionCall(response)
            return
        }

        val jwtResult: JwtResult = jwtTokenHandler.extractAllClaims(token)
        if (jwtResult.jwtResultType == JwtResultType.TOKEN_EXPIRED) {
            ErrorResponse(HttpStatus.UNAUTHORIZED, JwtResultType.TOKEN_EXPIRED.name)
                .exceptionCall(response)
            return
        }
        if (jwtResult.jwtResultType == JwtResultType.TOKEN_INVALID) {
            ErrorResponse(HttpStatus.UNAUTHORIZED, JwtResultType.TOKEN_INVALID.name)
                .exceptionCall(response)
            return
        }

        username = jwtResult.claims!!["username"].toString()

        val userDetails = userDetailService.loadUserByUsername(username)
        if (SecurityContextHolder.getContext().authentication == null) {
            val usernamePasswordAuthenticationToken =
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            usernamePasswordAuthenticationToken.details =
                WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            //session.setAttribute("userId", username);
        }

        filterChain.doFilter(request, response)

    }
}