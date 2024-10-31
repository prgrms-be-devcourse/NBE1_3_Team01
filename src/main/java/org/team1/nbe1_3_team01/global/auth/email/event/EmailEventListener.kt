package org.team1.nbe1_3_team01.global.auth.email.event

import jakarta.mail.MessagingException
import lombok.RequiredArgsConstructor
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.team1.nbe1_3_team01.global.auth.email.service.EmailService

@Component
@RequiredArgsConstructor
class EmailEventListener(private val emailService: EmailService) {
    @Async
    @EventListener
    @Throws(
        MessagingException::class
    )
    fun listen(event: EmailSendEvent) {
        emailService.sendSignUpLinkToEmail(event.email, event.courseId)
    }
}
