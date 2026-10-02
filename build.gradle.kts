plugins {
    id("java")
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway").version("9.22.3")
}

group = "com.edavalos.mtx.keystore"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.flywaydb:flyway-core:9.22.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    runtimeOnly("org.flywaydb:flyway-database-postgresql:10.1.0")
}

tasks.test {
    useJUnitPlatform()
}

flyway {
    url = "jdbc:postgresql://localhost:5432/mtxkvstore"
    user = "mtxkvstore"
    password = "mtxkvstore"
    baselineOnMigrate = true
}