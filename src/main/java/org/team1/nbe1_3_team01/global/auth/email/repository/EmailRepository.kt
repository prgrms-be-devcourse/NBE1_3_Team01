package org.team1.nbe1_3_team01.global.auth.email.repository

import org.springframework.data.repository.CrudRepository
import org.team1.nbe1_3_team01.global.auth.email.token.EmailToken

interface EmailRepository : CrudRepository<EmailToken, String> {
    fun existsByCode(code: String): Boolean
    fun findByCode(code: String): EmailToken?
}
