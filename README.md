<img width="242" alt="스크린샷 2024-11-06 오전 9 10 21" src="https://github.com/user-attachments/assets/88e1ec6f-4127-4fae-a15b-89e53bc52f6a"> <br>

<img width="896" alt="스크린샷 2024-11-06 오전 9 14 42" src="https://github.com/user-attachments/assets/b65afe21-9ec0-4114-b144-2471c59cc251">



## 👋 프로젝트 소개
DevWare는 데브코스 운영자, 수강생을 위한 그룹웨어입니다.



## 🤷‍ 기획배경
현재의 데브코스는 슬랙을 통해 공지사항이나 일정을 전파합니다. 코스 초반에는 노션이나 개인 연락 등으로 전파합니다. 이때, 전파사항의 채널이 분산되는 문제가 있습니다. 또한, 슬랙보다 보기 쉬운 게시판 및 캘린더 형식으로 일정, 공지사항을 전달할 수 있는 공간을 만들고자 했습니다. 참가자들끼리 단체 채팅도 가능하게 해, 데브코스 참가자를 위한 종합적인 기능과 소통 채널을 제공하는 그룹웨어를 기획했습니다.



## 🧑‍🤝‍🧑 참가인원 및 역할

<table>
  <tr>
    <td>
        <a href="https://github.com/power-minu">
            <img src="https://avatars.githubusercontent.com/power-minu?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/hs201016">
            <img src="https://avatars.githubusercontent.com/hs201016?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/sangcci">
            <img src="https://avatars.githubusercontent.com/sangcci?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/rinklove">
            <img src="https://avatars.githubusercontent.com/rinklove?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/jmd5314">
            <img src="https://avatars.githubusercontent.com/jmd5314?v=4" width="100px" />
        </a>
    </td>
  </tr>
  <tr>
    <td><b>김민우(팀장)</b></td>
    <td><b>류희수</b></td>
    <td><b>박상혁</b></td>
    <td><b>이준호</b></td>
    <td><b>조믿음</b></td>
  </tr>
  <tr>
    <td><b>팀 기능</b></td>
    <td><b>채팅</b></td>
    <td><b>캘린더</b></td>
    <td><b>게시판, 프론트</b></td>
    <td><b>인증</b></td>
  </tr>
</table>



## ⚙️ 주요기능
- 관리자는 코스를 만들 수 있다.
    - 코스 예시: 클라우드 기반 백엔드 엔지니어링 1기 1회차, 클라우드 기반 백엔드 엔지니어링 1기 2회차, ...
- 관리자는 수강생에게 회원가입 링크를 이메일로 전송할 수 있다.
  - 수강생은 해당 이메일의 링크로만 회원가입을 진행 할 수 있다.
- 관리자는 코스 게시판에 공지사항을 작성할 수 있다.
- 관리자는 특정 코스 내에 프로젝트 팀을 만들 수 있다.
    - 프로젝트 팀 예시: 1차 프로젝트 1팀, 1차 프로젝트 2팀, …
- 관리자는 특정 코스 관련 일정을 등록할 수 있다.
    - 일정 예시: 1차 프로젝트 제출 일정, 휴강, …
- 수강생은 한 코스 내에서 스터디 팀을 만들 수 있다.
- 수강생들은 팀 게시판에 글을 작성할 수 있다.
- 팀장은 팀 관련 일정을 등록할 수 있다.
    - 팀 일정 예시: RBF, 스터디 과제 일정, 스프린트 일정, …
- 관리자, 수강생들은 그룹 채팅방을 만들어 대화할 수 있다.
- <a href="https://www.notion.so/12ac41ac4b4e4cbb841e65a1c830318e?pvs=4">세부적인 요구사항 명세서</a>



## 🗒️ 다이어그램

<details>
  <summary><b>💽 ERD</b></summary>

  ![groupware](https://github.com/user-attachments/assets/1562fa47-01cc-4e76-93d8-8ca3a72d6f98)

</details>

<details>
  <summary><b>🔀 시퀀스 다이어그램</b></summary>

<a href="https://www.notion.so/c6647ebf7df34925b6672973e0a7f69c?pvs=4">시퀀스 다이어그램</a>

</details>



## 🛠️ 사용기술

### BE
![image](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![image](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=flat-square&logo=spring-boot)

### DB 접근
![Static Badge](https://img.shields.io/badge/Spring_data_JPA-lightgreen)
![Static Badge](https://img.shields.io/badge/QueryDsl-black)

### DB
![image](https://img.shields.io/badge/MySQL-005C84?style=flat-square&logo=mysql&logoColor=white)
<img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white">

### 실시간 기능
![Static Badge](https://img.shields.io/badge/websocket-ff9900)
![Static Badge](https://img.shields.io/badge/stomp-black)

### 인증 관련
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=Spring Security&logoColor=white">

### 기타
<img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=notion&logoColor=white"> <img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=slack&logoColor=white"> <img src="https://img.shields.io/badge/IntelliJ IDEA-4A154B?style=flat-square&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/Swagger-0?style=flat-square&logo=Swagger&logoColor=white&color=%2385EA2D">



## 📦 패키지 구조

<details>
  <summary><b>펼치기</b></summary>

```
📦NBE1_3_Team01
 ┗ 📂src
   ┣ 📂main
   ┃ ┣ 📂java
   ┃ ┃ ┗ 📂org
   ┃ ┃   ┗ 📂team1
   ┃ ┃     ┗ 📂nbe1_3_team01
   ┃ ┃       ┣ 📂domain
   ┃ ┃       ┃ ┣ 📂attendance
   ┃ ┃       ┃ ┃ ┣ 📂controller
   ┃ ┃       ┃ ┃ ┃ ┗ 📂response
   ┃ ┃       ┃ ┃ ┣ 📂entity
   ┃ ┃       ┃ ┃ ┣ 📂repository
   ┃ ┃       ┃ ┃ ┗ 📂service
   ┃ ┃       ┃ ┃   ┣ 📂dto
   ┃ ┃       ┃ ┃   ┣ 📂port
   ┃ ┃       ┃ ┃   ┗ 📂response
   ┃ ┃       ┃ ┣ 📂board
   ┃ ┃       ┃ ┃ ┣ 📂comment
   ┃ ┃       ┃ ┃ ┃ ┣ 📂controller
   ┃ ┃       ┃ ┃ ┃ ┃ ┗ 📂dto
   ┃ ┃       ┃ ┃ ┃ ┣ 📂repository
   ┃ ┃       ┃ ┃ ┃ ┗ 📂service
   ┃ ┃       ┃ ┃ ┃   ┣ 📂response
   ┃ ┃       ┃ ┃ ┃   ┗ 📂valid
   ┃ ┃       ┃ ┃ ┣ 📂constants
   ┃ ┃       ┃ ┃ ┣ 📂controller
   ┃ ┃       ┃ ┃ ┃ ┗ 📂dto
   ┃ ┃       ┃ ┃ ┣ 📂entity
   ┃ ┃       ┃ ┃ ┣ 📂exception
   ┃ ┃       ┃ ┃ ┣ 📂repository
   ┃ ┃       ┃ ┃ ┗ 📂service
   ┃ ┃       ┃ ┃   ┣ 📂converter
   ┃ ┃       ┃ ┃   ┣ 📂response
   ┃ ┃       ┃ ┃   ┗ 📂valid
   ┃ ┃       ┃ ┣ 📂calendar
   ┃ ┃       ┃ ┃ ┣ 📂application
   ┃ ┃       ┃ ┃ ┃ ┣ 📂port
   ┃ ┃       ┃ ┃ ┃ ┗ 📂response
   ┃ ┃       ┃ ┃ ┣ 📂controller
   ┃ ┃       ┃ ┃ ┃ ┗ 📂dto
   ┃ ┃       ┃ ┃ ┗ 📂entity
   ┃ ┃       ┃ ┣ 📂chat
   ┃ ┃       ┃ ┃ ┣ 📂controller
   ┃ ┃       ┃ ┃ ┃ ┗ 📂request
   ┃ ┃       ┃ ┃ ┣ 📂entity
   ┃ ┃       ┃ ┃ ┣ 📂repository
   ┃ ┃       ┃ ┃ ┗ 📂service
   ┃ ┃       ┃ ┃   ┗ 📂response
   ┃ ┃       ┃ ┣ 📂group
   ┃ ┃       ┃ ┃ ┣ 📂controller
   ┃ ┃       ┃ ┃ ┃ ┗ 📂request
   ┃ ┃       ┃ ┃ ┣ 📂entity
   ┃ ┃       ┃ ┃ ┣ 📂repository
   ┃ ┃       ┃ ┃ ┗ 📂service
   ┃ ┃       ┃ ┃   ┣ 📂response
   ┃ ┃       ┃ ┃   ┗ 📂validator
   ┃ ┃       ┃ ┗ 📂user
   ┃ ┃       ┃   ┣ 📂controller
   ┃ ┃       ┃   ┃ ┣ 📂api
   ┃ ┃       ┃   ┃ ┗ 📂request
   ┃ ┃       ┃   ┣ 📂entity
   ┃ ┃       ┃   ┣ 📂initializer
   ┃ ┃       ┃   ┣ 📂repository
   ┃ ┃       ┃   ┗ 📂service
   ┃ ┃       ┃     ┗ 📂response
   ┃ ┃       ┗ 📂global
   ┃ ┃         ┣ 📂advice
   ┃ ┃         ┣ 📂auth
   ┃ ┃         ┃ ┣ 📂email
   ┃ ┃         ┃ ┃ ┣ 📂controller
   ┃ ┃         ┃ ┃ ┃ ┗ 📂request
   ┃ ┃         ┃ ┃ ┣ 📂event
   ┃ ┃         ┃ ┃ ┣ 📂repository
   ┃ ┃         ┃ ┃ ┣ 📂service
   ┃ ┃         ┃ ┃ ┣ 📂token
   ┃ ┃         ┃ ┃ ┗ 📂util
   ┃ ┃         ┃ ┣ 📂interceptor
   ┃ ┃         ┃ ┣ 📂jwt
   ┃ ┃         ┃ ┃ ┣ 📂filter
   ┃ ┃         ┃ ┃ ┣ 📂respository
   ┃ ┃         ┃ ┃ ┣ 📂service
   ┃ ┃         ┃ ┃ ┗ 📂token
   ┃ ┃         ┃ ┗ 📂login
   ┃ ┃         ┃   ┣ 📂filter
   ┃ ┃         ┃   ┣ 📂handler
   ┃ ┃         ┃   ┗ 📂service
   ┃ ┃         ┣ 📂config
   ┃ ┃         ┣ 📂exception
   ┃ ┃         ┣ 📂util
   ┃ ┃         ┗ 📂validation
   ┃ ┗ 📂resources
   ┃   ┣ 📂dummy
   ┃   ┣ 📂static
   ┃   ┃ ┗ 📂emoticons
   ┃   ┗ 📂templates
   ┗ 📂test
     ┗ 📂java
       ┗ 📂org
         ┗ 📂team1
           ┗ 📂nbe1_3_team01
             ┗ 📂domain
               ┣ 📂attendance
               ┃ ┣ 📂entity
               ┃ ┣ 📂fake
               ┃ ┗ 📂service
               ┣ 📂group
               ┃ ┣ 📂fixture
               ┃ ┗ 📂service
               ┗ 📂user
                 ┣ 📂controller
                 ┗ 📂service
```

</details>
