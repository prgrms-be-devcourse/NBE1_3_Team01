package org.team1.nbe1_3_team01.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.auth.jwt.filter.JwtAuthenticationProcessingFilter
import org.team1.nbe1_3_team01.global.auth.jwt.respository.RefreshTokenRepository
import org.team1.nbe1_3_team01.global.auth.jwt.service.JwtService
import org.team1.nbe1_3_team01.global.auth.login.filter.CustomUsernamePasswordAuthenticationFilter
import org.team1.nbe1_3_team01.global.auth.login.handler.LoginFailureHandler
import org.team1.nbe1_3_team01.global.auth.login.handler.LoginSuccessHandler
import org.team1.nbe1_3_team01.global.auth.login.service.LoginService

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val loginService: LoginService,
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val objectMapper: ObjectMapper,
    private val webMvcConfig: WebMvcConfig

) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(webMvcConfig.corsConfigurationSource()) }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/api/user/sign-up/**").permitAll()
                    .requestMatchers("/user/sign-up/**").permitAll()
                    .requestMatchers("/static/**").permitAll()
                    .requestMatchers("/templates/**").permitAll()
                    .requestMatchers("/api/user/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/email/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/course/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/teams/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
        http.addFilterAfter(customUsernamePasswordAuthenticationFilter(), LogoutFilter::class.java)
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomUsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder())
        provider.setUserDetailsService(loginService)
        return ProviderManager(provider)
    }

    @Bean
    fun loginSuccessHandler(): LoginSuccessHandler {
        return LoginSuccessHandler(jwtService)
    }

    @Bean
    fun loginFailureHandler(): LoginFailureHandler {
        return LoginFailureHandler()
    }

    @Bean
    fun jwtAuthenticationProcessingFilter(): JwtAuthenticationProcessingFilter {
        return JwtAuthenticationProcessingFilter(jwtService, userRepository, refreshTokenRepository)
    }

    @Bean
    fun customUsernamePasswordAuthenticationFilter(): CustomUsernamePasswordAuthenticationFilter {
        val customUsernamePasswordAuthenticationFilter = CustomUsernamePasswordAuthenticationFilter(objectMapper)
        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager())
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler())
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler())
        return customUsernamePasswordAuthenticationFilter
    }
}
