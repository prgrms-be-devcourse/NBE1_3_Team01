package org.team1.nbe1_3_team01.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.team1.nbe1_3_team01.domain.user.service.CourseService;
import org.team1.nbe1_3_team01.global.auth.email.service.EmailService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SignupPageController {
    private final EmailService emailService;
    private final CourseService courseService;

    /**
     * 이메일 uuid 검증
     */
    @GetMapping("/sign-up")
    public String verifyUUID(@RequestParam String code,
                             @RequestParam Long courseId,
                             Model model) {
        boolean validCode = emailService.isValidCode(code);
        if (validCode) {
            model.addAttribute("email", emailService.findByCode(code).getEmail());
            model.addAttribute("courseName", courseService.findById(courseId).getName());
            model.addAttribute("courseId", courseId);
            return "signup";
        }
        model.addAttribute("error", "인증되지 않았거나 이미 회원가입된 이메일 입니다");
        return "error";
    }

}
