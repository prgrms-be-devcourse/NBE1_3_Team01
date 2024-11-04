package org.team1.nbe1_3_team01.global.auth.login.handler

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.team1.nbe1_3_team01.global.auth.jwt.service.JwtService
import java.io.IOException

class LoginSuccessHandler(private val jwtService: JwtService) : SimpleUrlAuthenticationSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val username = extractUsername(authentication)
        val accessToken = jwtService.createAccessToken(username)
        val refreshToken = jwtService.createRefreshToken(username)
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken)
    }

    private fun extractUsername(authentication: Authentication): String {
        val userDetails = authentication.principal as UserDetails
        return userDetails.username
    }
}
