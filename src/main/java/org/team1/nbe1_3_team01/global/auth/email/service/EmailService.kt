package org.team1.nbe1_3_team01.global.auth.email.service

import jakarta.mail.MessagingException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service
import org.team1.nbe1_3_team01.global.auth.email.repository.EmailRepository
import org.team1.nbe1_3_team01.global.auth.email.token.EmailToken
import org.team1.nbe1_3_team01.global.auth.email.util.EmailUtil
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    private val emailRepository: EmailRepository,
    private val retryTemplate: RetryTemplate
) {

    @OptIn(ExperimentalUuidApi::class)
    @Throws(MessagingException::class)
    fun sendSignUpLinkToEmail(email: String, courseId: Long) {
        val code = Uuid.random()
        val emailToken: EmailToken = EmailToken.of(
            email = email,
            code = code.toString()
        )

        emailRepository.save(emailToken)
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, EmailUtil.ENCODING)
        helper.setTo(email)
        helper.setSubject(EmailUtil.SUBJECT)
        // true 로 설정 하면 html 로 전송
        helper.setText(EmailUtil.createSignupContent(code, courseId), true)

        //retry 로직 적용
        retryTemplate.execute<Any?, RuntimeException> {
            mailSender.send(message)
            null
        }
    }

    fun isValidCode(code: String): Boolean {
        return emailRepository.existsByCode(code)
    }

    fun findByCode(code: String): EmailToken {
        return emailRepository.findByCode(code)
            ?: throw AppException(ErrorCode.CODE_NOT_FOUND)
    }

    fun deleteByEmail(email: String) {
        emailRepository.deleteById(email)
    }
}
