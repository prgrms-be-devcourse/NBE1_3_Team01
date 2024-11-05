package org.team1.nbe1_3_team01.domain.chat.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.team1.nbe1_3_team01.domain.chat.entity.Participant
import org.team1.nbe1_3_team01.domain.chat.entity.ParticipantPK
import java.util.*

@Repository
interface ParticipantRepository : JpaRepository<Participant?, ParticipantPK?> {
    fun findByUserId(userId: Long?): List<Participant?>?

    fun findByChannelId(channelId: Long?): List<Participant?>?

     fun findByUserIdAndChannelId(userId: Long?, channelId: Long?): Optional<Participant?>?

    fun findParticipantByUserIdAndChannelId(userId: Long, channelId: Long): Participant?

}
