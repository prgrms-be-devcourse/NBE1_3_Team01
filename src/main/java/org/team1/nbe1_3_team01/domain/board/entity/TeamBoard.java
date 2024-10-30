package org.team1.nbe1_3_team01.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.team1.nbe1_3_team01.domain.board.controller.dto.TeamBoardUpdateRequest;
import org.team1.nbe1_3_team01.domain.group.entity.Team;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "team_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    private Long readCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();


    @Builder
    private TeamBoard(
            Long categoryId,
            Team team,
            User user,
            String title,
            String content) {
        this.categoryId = categoryId;
        this.team = team;
        this.user = user;
        this.title = title;
        this.content = content;
        this.readCount = 0L;
        user.addTeamBoard(this);
        team.addTeamBoard(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }


    public void updateTeamBoard(TeamBoardUpdateRequest updateRequest) {
        String newTitle = updateRequest.title();
        String newContent = updateRequest.content();
        Long newCategoryId = updateRequest.categoryId();

        if(
                this.title.equals(newTitle) &&
                this.content.equals(newContent) &&
                this.categoryId.equals(newCategoryId)
        ) {
            throw new AppException(ErrorCode.BOARD_NOT_UPDATED);
        }

        this.categoryId = newCategoryId;
        this.title = newTitle;
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }
}
