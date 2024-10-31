package org.team1.nbe1_3_team01.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.team1.nbe1_3_team01.domain.user.entity.Course
import java.util.*

interface CourseRepository : JpaRepository<Course, Long> {
    fun findByName(name: String): Course?
}
