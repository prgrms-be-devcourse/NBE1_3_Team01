package org.team1.nbe1_3_team01.global.auth.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GroupAuth {

    Role role() default Role.TEAM;

    enum Role {
        ADMIN,
        COURSE,
        TEAM
    }
}
