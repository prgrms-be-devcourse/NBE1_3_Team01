package org.team1.nbe1_3_team01.domain.user.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.team1.nbe1_3_team01.domain.user.service.CourseService
import org.team1.nbe1_3_team01.global.auth.email.service.EmailService

@Controller
@RequestMapping("/user")
class SignupPageController(
    private val emailService: EmailService,
    private val courseService: CourseService
) {
    /**
     * 이메일 uuid 검증
     */
    @GetMapping("/sign-up")
    fun verifyUUID(
        @RequestParam code: String,
        @RequestParam courseId: Long,
        model: Model
    ): String {
        val validCode = emailService.isValidCode(code)
        if (validCode) {
            model.addAttribute("email", emailService.findByCode(code).email)
            model.addAttribute("courseName", courseService.findById(courseId).name)
            model.addAttribute("courseId", courseId)
            return "signup"
        }
        model.addAttribute("error", "인증되지 않았거나 이미 회원가입된 이메일 입니다")
        return "error"
    }
}
