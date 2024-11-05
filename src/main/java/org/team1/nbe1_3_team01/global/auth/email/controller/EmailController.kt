package org.team1.nbe1_3_team01.global.auth.email.controller

import jakarta.mail.MessagingException
import jakarta.validation.Valid
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.team1.nbe1_3_team01.global.auth.email.controller.request.EmailsRequest
import org.team1.nbe1_3_team01.global.auth.email.event.EmailSendEvent
import org.team1.nbe1_3_team01.global.util.Response

@RestController
@RequestMapping("/api/email")
class EmailController(private val publisher: ApplicationEventPublisher) {

    @PostMapping("/admin")
    @Throws(MessagingException::class)
    fun registerEmails(@RequestBody @Valid emailsRequest: EmailsRequest): ResponseEntity<Response<String>> {
        val courseId = emailsRequest.courseId
        for (emailRequest in emailsRequest.emails) {
            publisher.publishEvent(EmailSendEvent(emailRequest.email, courseId))
        }
        return ResponseEntity.ok().body(Response.success("메일 전송 완료"))
    }
}
