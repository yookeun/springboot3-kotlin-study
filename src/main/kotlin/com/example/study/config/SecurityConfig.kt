package com.example.study.config

import com.example.study.filter.JwtRequestFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(private val userDetailService: UserDetailService,
                     private val jwtRequestFilter: JwtRequestFilter) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
            .exceptionHandling { authenticationManager: ExceptionHandlingConfigurer<HttpSecurity?> ->
                authenticationManager
                    .authenticationEntryPoint(CustomAuthenticationEntryPoint())
                    .accessDeniedHandler(CustomAccessDeniedHandler())
            }
            .authenticationProvider(authenticationProvider())
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .cors(Customizer.withDefaults<CorsConfigurer<HttpSecurity>>())
            .sessionManagement { configurer: SessionManagementConfigurer<HttpSecurity?> ->
                configurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .authorizeHttpRequests {
                request -> request
                    .requestMatchers("/member/**").permitAll()
                    .requestMatchers("/docs/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

}