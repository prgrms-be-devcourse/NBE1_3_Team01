package org.team1.nbe1_3_team01.domain.chat.repository

import Chat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : JpaRepository<Chat?, Long?> {
    fun findByParticipant_Channel_Id(channelId: Long?): List<Chat?>?
}