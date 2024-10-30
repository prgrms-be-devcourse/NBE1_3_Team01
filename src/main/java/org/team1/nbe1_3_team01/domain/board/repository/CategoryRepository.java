package org.team1.nbe1_3_team01.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team1.nbe1_3_team01.domain.board.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, CustomCategoryRepository {

    int deleteByIdAndTeam_Id(Long id, Long teamId);
}
