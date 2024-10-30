-- USER

insert into course (id, name, is_delete)
values (1, 'Dev Course 1', 0),
       (2, 'Dev Course 2', 0);

insert into users (id, course_id, username, password, email, name, role, is_delete)
values (1, 1, 'alice', '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS',
        'alice@example.com', 'Alice', 'user', 0),
       (2, 1, 'bob', '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS',
        'bob@example.com', 'Bob', 'user', 0),
       (3, 1, 'charlie', '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS',
        'charlie@example.com', 'Charlie', 'user', 0),
       (4, 2, 'dave', '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS',
        'dave@example.com', 'Dave', 'user', 0),
       (5, 2, 'eve', '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS',
        'eve@example.com', 'Eve', 'user', 0),
       (6, 2, 'frank', '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS',
        'frank@example.com', 'Frank', 'user', 0),
       (7, 2, 'grace', '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS',
        'grace@example.com', 'Grace', 'user', 0),
       (8, 2, 'heidi', '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS',
        'heidi@example.com', 'Heidi', 'user', 0);


-- GROUP

insert into team (id, course_id, team_type, name, creation_waiting, deletion_waiting)
values (1, 1, 'PROJECT', 'PROJECT_TEAM_1', 0, 0),
       (2, 1, 'STUDY', 'STUDY_REAL_MYSQL', 0, 0),
       (3, 1, 'STUDY', 'STUDY_EFFECTIVE_JAVA', 0, 0),
       (4, 1, 'PROJECT', 'PROJECT_TEAM_2', 0, 0);

insert into belonging (id, team_id, user_id, is_owner)
values (1, 1, 1, 1), -- Alice is the owner of PROJECT_TEAM_1
       (2, 1, 2, 0), -- Bob is a member of PROJECT_TEAM_1
       (3, 2, 3, 1), -- Charlie is the owner of STUDY_REAL_MYSQL
       (4, 4, 4, 1), -- Dave is the owner of PROJECT_TEAM_2
       (5, 4, 5, 0), -- Eve is a member of PROJECT_TEAM_2
       (6, 2, 6, 0), -- Frank is a member of STUDY_REAL_MYSQL
       (7, 3, 7, 1), -- Grace is the owner of STUDY_EFFECTIVE_JAVA
       (8, 3, 8, 0), -- Heidi is a member of STUDY_EFFECTIVE_JAVA
       (9, 2, 1, 0), -- Alice is a member of STUDY_REAL_MYSQL
       (10, 2, 2, 0);
-- Bob is a member of STUDY_REAL_MYSQL


-- CHAT

insert into channel (ID, CHANNEL_NAME, CREATED_AT)
values (1, 'General Chat', '2024-10-09 12:00:00'),
       (2, 'PROJECT_TEAM_1 Chat', '2024-10-09 13:00:00'),
       (3, 'PROJECT_TEAM_2 Chat', '2024-10-10 13:00:00');

insert into participant (USER_ID, CHANNEL_ID, IS_CREATOR, PARTICIPATED_AT, IS_PARTICIPATED)
values (1, 1, 1, '2024-10-09 12:10:00', 1), -- Alice created General Chat
       (2, 1, 0, '2024-10-09 12:15:00', 1), -- Bob joined General Chat
       (3, 2, 1, '2024-10-09 13:05:00', 1), -- Charlie created PROJECT_TEAM_1 Chat
       (4, 3, 1, '2024-10-10 13:10:00', 1), -- Dave created PROJECT_TEAM_2 Chat
       (5, 2, 0, '2024-10-10 13:15:00', 1), -- Eve joined PROJECT_TEAM_1 Chat
       (6, 2, 0, '2024-10-10 13:20:00', 1); -- Frank joined PROJECT_TEAM_1 Chat

insert into chat (ID, USER_ID, CHANNEL_ID, CONTENT, CREATED_AT)
values (1, 1, 1, 'Hello everyone!', '2024-10-09 12:20:00'),
       (2, 2, 1, 'Hi Alice!', '2024-10-09 12:25:00'),
       (3, 4, 3, 'Welcome to PROJECT_TEAM_2 Chat', '2024-10-10 13:15:00');


-- SCHEDULE

insert into course_schedule (id, course_id, name, description, start_at, end_at)
values (1, 1, 'Course Orientation', '오리엔테이션', '2024-10-01 09:00:00',
        '2024-10-01 18:00:00'),
       (2, 1, 'Guest Lecture', '특강 - git 사용법', '2024-10-05 09:00:00',
        '2024-10-05 13:00:00'),
       (3, 2, 'Project Announcement', '1차 프로젝트 결과 발표', '2024-10-10 14:00:00',
        '2024-10-10 16:00:00');

insert into team_schedule (id, team_id, name, schedule_type, description, start_at, end_at)
values (1, 1, 'PROJECT_TEAM_1 Scrum', 'SCRUM', '10-10 스크럼',
        '2024-10-10 17:00:00', '2024-10-10 17:30:00'),
       (2, 2, 'STUDY_REAL_MYSQL Meeting', 'MEETING', '10장 실행계획 스터디',
        '2024-10-11 14:00:00', '2024-10-11 15:00:00'),
       (3, 4, 'PROJECT_TEAM_2 Scrum', 'SCRUM', '10-12 스크럼',
        '2024-10-12 18:00:00', '2024-10-12 18:30:00');


-- ATTENDANCE

insert into attendance (id, user_id, type, start_at, end_at, description, creation_waiting)
values (1, 1, 'LATE', '2024-10-10 09:05:00', '2024-10-10 09:15:00', '몸살', 1),
       (2, 2, 'OUTING', '2024-10-10 10:00:00', '2024-10-10 11:00:00', '국취제 상담', 0),
       (3, 4, 'EARLY', '2024-10-12 15:00:00', '2024-10-12 15:30:00', '학교 수업', 1),
       (4, 5, 'ABSENT', '2024-10-13 09:00:00', '2024-10-13 17:00:00', '가족 일정', 0);


-- BOARD

insert into course_board (id, course_id, user_id, title, content, created_at, updated_at,
                          read_count)
values (1, 1, 1, 'Course Introduction', 'Welcome to Dev Course 1', '2024-10-01 08:00:00',
        '2024-10-01 09:00:00', 20),
       (2, 2, 4, 'Course Announcement', 'New schedule update', '2024-10-05 09:00:00',
        '2024-10-05 10:00:00', 30);

insert into team_board (id, user_id, team_id, category_id, title, content, created_at, updated_at,
                        read_count)
values (1, 1, 1, null, '팀 컨벤션', '컨벤션은 다음과 같습니다~', '2024-10-10 09:00:00',
        '2024-10-10 09:30:00', 10),
       (2, 3, 2, null, '스터디 규칙', '스터디 규칙은 다음과 같습니다~', '2024-10-11 10:00:00',
        '2024-10-11 10:45:00', 15);

insert into comment (comment_id, user_id, board_id, content, created_at, updated_at)
values (1, 1, 1, '도움이 많이 되었어요!', '2024-10-09 14:00:00', '2024-10-09 14:05:00'),
       (2, 2, 1, '공유해주셔서 감사합니다.', '2024-10-09 14:10:00', '2024-10-09 14:15:00'),
       (3, 4, 2, '제가 찾고 싶던 주제였어요.', '2024-10-10 15:00:00', '2024-10-10 15:05:00'),
       (4, 5, 2, '작성자의 의견에 동의합니다.', '2024-10-10 15:10:00', '2024-10-10 15:15:00');