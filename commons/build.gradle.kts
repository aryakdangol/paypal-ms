plugins {
    id("java-library")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "com.payments"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.auth0:java-jwt:4.4.0")

}