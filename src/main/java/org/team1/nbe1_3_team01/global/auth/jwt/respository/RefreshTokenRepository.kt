package org.team1.nbe1_3_team01.global.auth.jwt.respository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.team1.nbe1_3_team01.global.auth.jwt.token.RefreshToken
import java.util.*

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, String> {
    fun findByToken(token: String): RefreshToken?
}
