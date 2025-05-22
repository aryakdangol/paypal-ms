plugins {
    id("java")
    id("org.springframework.boot") version "3.4.5" apply false
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.example"
version = "1.0-SNAPSHOT"

allprojects {
    group = "com.payments"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        runtimeOnly("com.h2database:h2")
        implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
        implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.1")
        }
    }

}


