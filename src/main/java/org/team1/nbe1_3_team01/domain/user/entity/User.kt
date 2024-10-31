package org.team1.nbe1_3_team01.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.team1.nbe1_3_team01.domain.board.entity.CourseBoard;
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard;
import org.team1.nbe1_3_team01.domain.board.entity.Comment;
import org.team1.nbe1_3_team01.domain.chat.entity.Participant;
import org.team1.nbe1_3_team01.domain.group.entity.Belonging;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@SQLRestriction("is_delete = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    @NotNull
    private String username;

    @NotNull
    private String password;

    @Column(length = 50)
    @NotNull
    private String email;

    @Column(length = 10)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isDelete;

    @OneToMany(mappedBy = "user")
    private List<Belonging> belongings = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<TeamBoard> teamBoards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CourseBoard> courseBoards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Participant> participants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;


    @Builder
    private User(String username,
                 String password,
                 String email,
                 String name,
                 Role role,
                 Course course
    ) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
        this.isDelete = false;
        this.course = course;
    }

    public void addTeamBoard(TeamBoard teamBoard) {
        this.teamBoards.add(teamBoard);
    }

    public void addCourseBoard(CourseBoard courseBoard){
        this.courseBoards.add(courseBoard);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }

    public void addBelonging(Belonging belonging) {
        this.belongings.add(belonging);
    }

    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void delete(){
        this.isDelete = true;
    }
}
