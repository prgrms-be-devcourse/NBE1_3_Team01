package org.team1.nbe1_3_team01.global.auth.interceptor

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class GroupAuth(
    val role: Role = Role.TEAM
) {
    enum class Role {
        ADMIN,
        COURSE,
        TEAM
    }
}
