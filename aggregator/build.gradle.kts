plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.payments"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":Gateway"))
    implementation(project(":Payment"))
    implementation(project(":Notification"))
}
