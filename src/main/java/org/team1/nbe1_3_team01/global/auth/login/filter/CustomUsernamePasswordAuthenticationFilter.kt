package org.team1.nbe1_3_team01.global.auth.login.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.util.StreamUtils
import java.io.IOException
import java.nio.charset.StandardCharsets

class CustomUsernamePasswordAuthenticationFilter(private val objectMapper: ObjectMapper) :
    AbstractAuthenticationProcessingFilter(PATH_REQUEST_MATCHER) {
    @Throws(
        AuthenticationException::class,
        IOException::class,
        ServletException::class
    )
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        if (request.contentType == null || request.contentType != CONTENT_TYPE) {
            throw AuthenticationServiceException("ContentType이 " + request.contentType + " 가 아닙니다")
        }

        val messageBody = StreamUtils.copyToString(request.inputStream, StandardCharsets.UTF_8)
        val usernamePasswordMap = objectMapper.readValue(
            messageBody,
            MutableMap::class.java
        ) as Map<*, *>
        val username = usernamePasswordMap[USERNAME_KEY]
        val password = usernamePasswordMap[PASSWORD_KEY]
        //principal 과 credentials 전달
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(username, password)

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken)
    }

    companion object {
        private const val REQUEST_URL = "/api/login"
        private const val HTTP_METHOD = "POST"
        private const val CONTENT_TYPE = "application/json"
        private const val USERNAME_KEY = "username"
        private const val PASSWORD_KEY = "password"

        // /login + POST 요청에 매칭
        private val PATH_REQUEST_MATCHER = AntPathRequestMatcher(REQUEST_URL, HTTP_METHOD)
    }
}
