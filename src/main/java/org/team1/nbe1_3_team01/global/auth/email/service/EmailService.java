package org.team1.nbe1_3_team01.global.auth.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.team1.nbe1_3_team01.global.auth.email.util.EmailUtil;
import org.team1.nbe1_3_team01.global.auth.email.repository.EmailRepository;
import org.team1.nbe1_3_team01.global.auth.email.token.EmailToken;
import org.team1.nbe1_3_team01.global.exception.AppException;

import java.util.UUID;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.CODE_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private final RetryTemplate retryTemplate;


    public void sendSignUpLinkToEmail(String email, Long courseId) throws MessagingException {
        UUID code = UUID.randomUUID();
        EmailToken emailToken = EmailToken.builder()
                .email(email)
                .code(String.valueOf(code))
                .build();
        emailRepository.save(emailToken);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, EmailUtil.getEncoding());
        helper.setTo(email);
        helper.setSubject(EmailUtil.getSubject());
        // true 로 설정 하면 html 로 전송
        helper.setText(EmailUtil.createSignupContent(code, courseId), true);

        //retry 로직 적용
        retryTemplate.execute(retryContext -> {
            mailSender.send(message);
            log.info("{} 메일 전송 완료", email);
            return null;
        });
    }


    public boolean isValidCode(String code) {
        boolean exists = emailRepository.existsByCode(code);
        log.info("검증하려는 UUID: {}, 존재 여부: {}", code, exists);
        return exists;
    }

    public EmailToken findByCode(String code) {
        return emailRepository.findByCode(code)
                .orElseThrow(() -> new AppException(CODE_NOT_FOUND));
    }

    public void deleteByEmail(String email) {
        emailRepository.deleteById(email);
    }
}
