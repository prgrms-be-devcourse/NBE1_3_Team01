package org.team1.nbe1_3_team01.global.auth.email.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_3_team01.global.auth.email.controller.request.EmailsRequest;
import org.team1.nbe1_3_team01.global.auth.email.event.EmailSendEvent;
import org.team1.nbe1_3_team01.global.util.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {
    private final ApplicationEventPublisher publisher;

    @PostMapping("/admin")
    public ResponseEntity<Response<String>> registerEmails(@Valid @RequestBody EmailsRequest emailsRequest) throws MessagingException {
        Long courseId = emailsRequest.courseId();
        for (String email : emailsRequest.emails()) {
            publisher.publishEvent(new EmailSendEvent(email, courseId));
        }
        return ResponseEntity.ok().body(Response.success("메일 전송 완료"));
    }

}
