plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.jpa") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"
    kotlin("kapt") version "2.0.21"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

group = "org.team1"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Retry
    implementation("org.springframework.retry:spring-retry:2.0.9")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Email
    implementation("org.springframework.boot:spring-boot-starter-mail")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket:3.0.0")
    implementation("org.webjars:sockjs-client:1.5.1")
    implementation("org.webjars:stomp-websocket:2.3.4")
    implementation("org.springframework:spring-messaging")

    compileOnly("org.projectlombok:lombok")
    kapt("org.projectlombok:lombok")

    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    runtimeOnly("com.h2database:h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // QueryDSL 설정
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

// QueryDSL 설정부
val generated = "src/main/generated"

kapt {
    correctErrorTypes = true
    // QueryDSL QClass 파일 생성 위치를 지정
    arguments {
        arg("querydsl.sources", generated) // src/main/generated
    }
}

// Java source set에 QueryDSL QClass 위치 추가
sourceSets {
    main {
        java {
            srcDir(generated) // 생성된 QClass 파일 위치 추가
        }
    }
}

// gradle clean 시에 QClass 디렉토리 삭제
tasks.named<Delete>("clean") {
    delete(file(generated))
}