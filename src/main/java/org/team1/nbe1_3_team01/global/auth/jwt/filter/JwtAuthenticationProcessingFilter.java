package org.team1.nbe1_3_team01.global.auth.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.global.auth.jwt.service.JwtService;
import org.team1.nbe1_3_team01.global.auth.jwt.respository.RefreshTokenRepository;
import org.team1.nbe1_3_team01.global.util.ErrorCode;

import java.io.IOException;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.*;

/**
 * Jwt 인증 필터
 * RTR 방식으로 동작
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공
 * 2. RefreshToken이 없고, AccessToken이 없거나 유효X 인 경우 -> 인증 실패
 * 3. 재발급 요청 시 RefreshToken 검증 유효하면 AccessToken, RefreshToken 재발급
 */
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private static final String LOGIN_URI = "/api/login";
    private static final String REISSUE_URI = "/api/reissue-token";
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // 로그인 요청 들어오면 다음 필터 호출
        if (isLoginRequest(requestURI)) {
            handleLoginRequest(request, response, filterChain);
            return;
        }

        // 재발급 요청 들어오면 refreshToken 유효성 검사 한 뒤 유효하면 AccessToken 및 RefreshToken 재발급
        if (isReissueRequest(requestURI)) {
            handleTokenReissueRequest(request, response);
            return;
        }

        handleAccessTokenValidation(request, response, filterChain);
    }

    private boolean isLoginRequest(String requestURI) {
        return LOGIN_URI.equals(requestURI);
    }

    private boolean isReissueRequest(String requestURI) {
        return REISSUE_URI.equals(requestURI);
    }

    private void handleLoginRequest(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }

    private void handleTokenReissueRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // GET 요청이 아닌 경우 에러 메세지 반환
        if(!request.getMethod().equals("GET")){
            sendErrorResponse(response, REQUEST_INVALID);
            return;
        }
        String refreshToken = jwtService.extractRefreshToken(request)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("RefreshToken이 없습니다"));
        try {
            jwtService.validateToken(refreshToken);
            if (refreshToken != null) {
                checkRefreshTokenAndReIssueAccessTokenAndRefreshToken(response, refreshToken);
            }
        } catch (Exception e) {
            sendErrorResponse(response, TOKEN_INVALID);
        }
    }

    private void checkRefreshTokenAndReIssueAccessTokenAndRefreshToken(HttpServletResponse response, String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresentOrElse(token -> {
                            String username = token.getUsername();
                            userRepository.findByUsername(username)
                                    .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다"));
                            String reIssuedRefreshToken = jwtService.createRefreshToken(username);
                            jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(username), reIssuedRefreshToken);
                        },
                        () -> {
                            try {
                                sendErrorResponse(response, TOKEN_NOT_EXIST);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
    }

    private void handleAccessTokenValidation(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String accessToken = jwtService.extractAccessToken(request)
                .orElse(null);

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            authenticationUser(accessToken);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, TOKEN_TIMEOUT);
        } catch (Exception e) {
            sendErrorResponse(response, TOKEN_INVALID);
        }
    }


    private void authenticationUser(String accessToken) {
        jwtService.validateToken(accessToken);
        jwtService.extractUsername(accessToken)
                .flatMap(userRepository::findByUsername)
                .ifPresent(this::saveAuthentication);
    }

    private void saveAuthentication(User user) {
        String password = user.getPassword();

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(password)
                .roles(user.getRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(errorCode.getMessage());

    }

}
