plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-security") // if using JWT
    implementation(project(":commons"))
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
}