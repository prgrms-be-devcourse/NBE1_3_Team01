package org.team1.nbe1_3_team01.domain.group.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.team1.nbe1_3_team01.domain.board.entity.Category;
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard;
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule;
import org.team1.nbe1_3_team01.domain.user.entity.Course;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TeamType teamType;

    @Column(length = 50)
    @Setter
    private String name;

    @Column(columnDefinition = "TINYINT(1)")
    @Setter
    private boolean creationWaiting;

    @Column(columnDefinition = "TINYINT(1)")
    @Setter
    private boolean deletionWaiting;

    @OneToMany(
            mappedBy = "team",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Belonging> belongings = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<TeamBoard> teamBoards = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<TeamSchedule> teamSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Category> categories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder
    private Team(
            Course course,
            TeamType teamType,
            String name,
            boolean creationWaiting,
            boolean deletionWaiting) {
        this.course = course;
        this.teamType = teamType;
        this.name = name;
        this.creationWaiting = creationWaiting;
        this.deletionWaiting = deletionWaiting;
        course.addTeam(this);
    }

    public void assignBelonging(Belonging belonging) {
        this.belongings.add(belonging);
        belonging.assignTeam(this);
    }

    public void addTeamBoard(TeamBoard teamBoard) {
        this.teamBoards.add(teamBoard);
    }

    public void addTeamScheDule(TeamSchedule teamSchedule) {
        this.teamSchedules.add(teamSchedule);
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }
}
