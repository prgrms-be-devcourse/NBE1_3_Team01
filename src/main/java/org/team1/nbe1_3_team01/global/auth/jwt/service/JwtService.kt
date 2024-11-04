package org.team1.nbe1_3_team01.global.auth.jwt.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.team1.nbe1_3_team01.global.auth.jwt.respository.RefreshTokenRepository
import org.team1.nbe1_3_team01.global.auth.jwt.token.RefreshToken
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Service
class JwtService(
    private val refreshTokenRepository: RefreshTokenRepository
) {
    @Value("\${jwt.secretKey}")
    private lateinit var secretKey: String

    @Value("\${jwt.access.expiration}")
    private var accessTokenExpirationPeriod: Long? = null

    @Value("\${jwt.refresh.expiration}")
    private var refreshTokenExpirationPeriod: Long? = null

    @Value("\${jwt.access.header}")
    private lateinit var accessHeader: String

    @Value("\${jwt.refresh.header}")
    private lateinit var refreshHeader: String

    companion object {
        private const val ACCESS_TOKEN_SUBJECT = "AccessToken"
        private const val REFRESH_TOKEN_SUBJECT = "RefreshToken"
        private const val USERNAME_CLAIM = "username"
        private const val BEARER = "Bearer "
    }

    private fun getSigningKey(): Key {
        val decodedKey = Base64.getDecoder().decode(secretKey)
        return SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.jcaName)
    }


    /**
     * AccessToken 생성 메서드
     */
    fun createAccessToken(username: String): String {
        val now = Date()
        val claims = Jwts.claims()
        claims[USERNAME_CLAIM] = username
        claims.setExpiration(Date(now.time + accessTokenExpirationPeriod!!))
        return Jwts.builder()
            .setSubject(ACCESS_TOKEN_SUBJECT)
            .setClaims(claims)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * RefreshToken 생성 메서드
     * 생성 후 refreshTokenRepository에 저장
     */
    fun createRefreshToken(username: String): String {
        val now = Date()
        val claims = Jwts.claims()
        claims.setExpiration(Date(now.time + refreshTokenExpirationPeriod!!))
        val token = Jwts.builder()
            .setSubject(REFRESH_TOKEN_SUBJECT)
            .setClaims(claims)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()

        val refreshToken = RefreshToken.of(
            username = username,
            token = token
        )

        refreshTokenRepository.save(refreshToken)
        return token
    }

    /**
     * RefreshToken과 AccessToken을 응답 헤더에 넣어주는 메서드
     */
    fun sendAccessAndRefreshToken(response: HttpServletResponse, accessToken: String, refreshToken: String) {
        response.status = HttpServletResponse.SC_OK
        setAccessTokenHeader(response, accessToken)
        setRefreshTokenHeader(response, refreshToken)
    }

    /**
     * Bearer 를 제거하여 refreshToken만 추출해 내는 메서드
     */
    fun extractRefreshToken(request: HttpServletRequest): String? {
        val refreshToken = request.getHeader(refreshHeader)
        return if (refreshToken != null && refreshToken.startsWith(BEARER)) {
            refreshToken.replace(BEARER, "")
        } else null

    }

    /**
     * Bearer 를 제거하여 accessToken만 추출해 내는 메서드
     */
    fun extractAccessToken(request: HttpServletRequest): String? {
        val accessToken = request.getHeader(accessHeader)
        return if (accessToken != null && accessToken.startsWith(BEARER)) {
            accessToken.replace(BEARER, "")
        } else null
    }

    /**
     * accessToken에서 username을 추출해 내는 메서드
     */
    fun extractUsername(accessToken: String?): String? {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(accessToken)
                .body

            return claims[USERNAME_CLAIM, String::class.java]
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * AccessToken을 응답 헤더에 넣어주는 메서드
     */
    private fun setAccessTokenHeader(response: HttpServletResponse, accessToken: String) {
        response.setHeader(accessHeader, accessToken)
    }

    /**
     * RefreshToken을 응답 헤더에 넣어주는 메서드
     */
    private fun setRefreshTokenHeader(response: HttpServletResponse, refreshToken: String) {
        response.setHeader(refreshHeader, refreshToken)
    }

    /**
     * 해당 토큰이 유효한지 검증하는 메서드
     */
    fun validateToken(token: String) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
        } catch (e: Exception) {
            throw e
        }
    }

}
