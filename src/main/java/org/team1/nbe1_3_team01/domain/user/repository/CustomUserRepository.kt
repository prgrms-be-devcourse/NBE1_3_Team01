package org.team1.nbe1_3_team01.domain.user.repository;

import org.team1.nbe1_3_team01.domain.user.entity.User;

import java.util.List;

public interface CustomUserRepository {

    List<User> findAllUsersByIdList(List<Long> userIds);

    List<User> findUsersAndAdminsByCourseId(Long courseId);
}
