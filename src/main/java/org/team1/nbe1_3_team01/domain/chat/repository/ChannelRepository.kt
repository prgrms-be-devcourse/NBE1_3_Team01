package org.team1.nbe1_3_team01.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.team1.nbe1_3_team01.domain.chat.entity.Channel;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long>{

    @Query("SELECT c.channelName FROM Channel c")
    List<String> findAllChannelName();
}
