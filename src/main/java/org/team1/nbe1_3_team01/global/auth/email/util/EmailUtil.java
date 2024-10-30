package org.team1.nbe1_3_team01.global.auth.email.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailUtil {
    private static final String ENCODING = "UTF-8";
    private static final String SUBJECT = "데브코스 회원가입 링크";
    private static final String CONTENT = "<p>회원가입을 하려면 <a href=\"%s\">여기</a>를 클릭하세요</p>";
    private static final String SIGNUP_URL = "http://localhost:8080/user/sign-up";
    private static final String CODE = "?code=";
    private static final String COURSE_ID = "&courseId=";

    public static String createSignupContent(UUID uuid,Long courseId){
        return String.format(CONTENT, SIGNUP_URL + CODE + uuid + COURSE_ID + courseId);
    }

    public static String getSubject(){
        return SUBJECT;
    }

    public static String getEncoding(){
        return ENCODING;
    }
}
