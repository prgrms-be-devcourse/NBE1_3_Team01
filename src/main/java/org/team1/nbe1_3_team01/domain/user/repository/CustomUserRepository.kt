package org.team1.nbe1_3_team01.domain.user.repository

import org.team1.nbe1_3_team01.domain.user.entity.User

interface CustomUserRepository {
    fun findAllUsersByIdList(userIds: List<Long>): List<User>

    fun findUsersAndAdminsByCourseId(courseId: Long): List<User>
}
