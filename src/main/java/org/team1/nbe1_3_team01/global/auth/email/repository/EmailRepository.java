package org.team1.nbe1_3_team01.global.auth.email.repository;

import org.springframework.data.repository.CrudRepository;
import org.team1.nbe1_3_team01.global.auth.email.token.EmailToken;

import java.util.Optional;

public interface EmailRepository extends CrudRepository<EmailToken,String> {
    boolean existsByCode(String code);
    Optional<EmailToken> findByCode(String code);

}
