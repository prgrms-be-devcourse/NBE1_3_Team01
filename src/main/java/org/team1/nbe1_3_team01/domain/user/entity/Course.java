package org.team1.nbe1_3_team01.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.team1.nbe1_3_team01.domain.board.entity.CourseBoard;
import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule;
import org.team1.nbe1_3_team01.domain.group.entity.Team;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "course")
@SQLRestriction("is_delete = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isDelete;

    @OneToMany(mappedBy = "course")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Team> teams = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<CourseSchedule> courseSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<CourseBoard> courseBoards = new ArrayList<>();

    @Builder
    private Course(String name) {
        this.name = name;
        this.isDelete = false;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void addTeam(Team team){
        this.teams.add(team);
    }

    public void addCourseBoard(CourseBoard courseBoard) {
        this.courseBoards.add(courseBoard);
    }

    public void addCourseSchedule(CourseSchedule courseSchedule){
        this.courseSchedules.add(courseSchedule);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void delete(){
        this.isDelete = true;
    }
}
