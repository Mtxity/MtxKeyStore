plugins {
    id("java")
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.edavalos.mtx.keystore"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4")
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}