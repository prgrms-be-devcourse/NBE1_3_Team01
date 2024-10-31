package org.team1.nbe1_3_team01.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailConfig {
    @Value("\${spring.mail.host}")
    private val host: String? = null

    @Value("\${spring.mail.port}")
    private val port = 0

    @Value("\${spring.mail.username}")
    private val username: String? = null

    @Value("\${spring.mail.password}")
    private val password: String? = null

    @Value("\${spring.mail.properties.mail.smtp.auth}")
    private val mailSmtpAuth = false

    @Value("\${spring.mail.properties.mail.smtp.starttls.enable}")
    private val mailSmtpStarttlsEnable = false

    @Bean
    fun mailSender(): JavaMailSenderImpl {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = username
        mailSender.password = password

        val props = mailSender.javaMailProperties
        props["mail.smtp.auth"] = mailSmtpAuth.toString()
        props["mail.smtp.starttls.enable"] = mailSmtpStarttlsEnable.toString()
        props["mail.transport.protocol"] = "smtp"
        props["mail.debug"] = "true"

        return mailSender
    }
}
