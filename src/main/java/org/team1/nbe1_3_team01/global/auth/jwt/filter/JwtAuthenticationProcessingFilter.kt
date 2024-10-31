package org.team1.nbe1_3_team01.global.auth.jwt.filter

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.filter.OncePerRequestFilter
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.auth.jwt.respository.RefreshTokenRepository
import org.team1.nbe1_3_team01.global.auth.jwt.service.JwtService
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import java.io.IOException
import java.util.*

/**
 * Jwt 인증 필터
 * RTR 방식으로 동작
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공
 * 2. RefreshToken이 없고, AccessToken이 없거나 유효X 인 경우 -> 인증 실패
 * 3. 재발급 요청 시 RefreshToken 검증 유효하면 AccessToken, RefreshToken 재발급
 */
@RequiredArgsConstructor
class JwtAuthenticationProcessingFilter(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) : OncePerRequestFilter() {
    private val authoritiesMapper: GrantedAuthoritiesMapper = NullAuthoritiesMapper()

    companion object {
        private const val LOGIN_URI = "/api/login"
        private const val REISSUE_URI = "/api/reissue-token"
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestURI = request.requestURI

        // 로그인 요청 들어오면 다음 필터 호출
        if (isLoginRequest(requestURI)) {
            handleLoginRequest(request, response, filterChain)
            return
        }

        // 재발급 요청 들어오면 refreshToken 유효성 검사 한 뒤 유효하면 AccessToken 및 RefreshToken 재발급
        if (isReissueRequest(requestURI)) {
            handleTokenReissueRequest(request, response)
            return
        }

        handleAccessTokenValidation(request, response, filterChain)
    }

    private fun isLoginRequest(requestURI: String): Boolean {
        return LOGIN_URI == requestURI
    }

    private fun isReissueRequest(requestURI: String): Boolean {
        return REISSUE_URI == requestURI
    }

    @Throws(ServletException::class, IOException::class)
    private fun handleLoginRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        filterChain.doFilter(request, response)
    }

    @Throws(IOException::class)
    private fun handleTokenReissueRequest(request: HttpServletRequest, response: HttpServletResponse) {
        // GET 요청이 아닌 경우 에러 메세지 반환
        if (request.method != "GET") {
            sendErrorResponse(response, ErrorCode.REQUEST_INVALID)
            return
        }
        val refreshToken = jwtService.extractRefreshToken(request)
            ?: throw AuthenticationCredentialsNotFoundException("RefreshToken이 없습니다")

        try {
            jwtService.validateToken(refreshToken)
            checkRefreshTokenAndReIssueAccessTokenAndRefreshToken(response, refreshToken)
        } catch (e: Exception) {
            sendErrorResponse(response, ErrorCode.TOKEN_INVALID)
        }
    }

    private fun checkRefreshTokenAndReIssueAccessTokenAndRefreshToken(
        response: HttpServletResponse,
        refreshToken: String
    ) {
        val token = refreshTokenRepository.findByToken(refreshToken)
        if (token != null) {
            val username = token.username
            userRepository.findByUsername(username)
                ?: throw UsernameNotFoundException("해당 사용자가 존재하지 않습니다")
            jwtService.sendAccessAndRefreshToken(response,jwtService.createAccessToken(username),jwtService.createRefreshToken(username))
        }
        else{
            sendErrorResponse(response,ErrorCode.TOKEN_NOT_EXIST)
        }

    }

    @Throws(IOException::class, ServletException::class)
    private fun handleAccessTokenValidation(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = jwtService.extractAccessToken(request)

        if (accessToken == null) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            authenticationUser(accessToken)
            filterChain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            sendErrorResponse(response, ErrorCode.TOKEN_TIMEOUT)
        } catch (e: Exception) {
            sendErrorResponse(response, ErrorCode.TOKEN_INVALID)
        }
    }


    private fun authenticationUser(accessToken: String) {
        jwtService.validateToken(accessToken)
        val username = jwtService.extractUsername(accessToken)
            ?: throw AppException(ErrorCode.TOKEN_INVALID)
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.")
        saveAuthentication(user)
    }

    private fun saveAuthentication(user: User) {
        val password = user.password

        val userDetailsUser = org.springframework.security.core.userdetails.User.builder()
            .username(user.username)
            .password(password)
            .roles(user.role.name)
            .build()

        val authentication: Authentication =
            UsernamePasswordAuthenticationToken(
                userDetailsUser, null,
                authoritiesMapper.mapAuthorities(userDetailsUser.authorities)
            )

        SecurityContextHolder.getContext().authentication = authentication
    }

    @Throws(IOException::class)
    private fun sendErrorResponse(response: HttpServletResponse, errorCode: ErrorCode) {
        response.status = errorCode.status.value()
        response.characterEncoding = "UTF-8"
        response.contentType = "text/plain;charset=UTF-8"
        response.writer.write(errorCode.getMessage())
    }

}
