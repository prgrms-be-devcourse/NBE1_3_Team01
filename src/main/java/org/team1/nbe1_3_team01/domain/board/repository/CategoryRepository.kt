package org.team1.nbe1_3_team01.domain.board.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.team1.nbe1_3_team01.domain.board.entity.Category

@Repository
interface CategoryRepository : CrudRepository<Category, Long>, CustomCategoryRepository {
    fun deleteByIdAndTeam_Id(id: Long, teamId: Long): Int
}
