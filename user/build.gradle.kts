plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)

    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "me.ankur-varma"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

repositories {
    mavenCentral()
}

dependencies {
//    implementation(libs.spring.boot.actuator)
//    implementation(libs.spring.boot.amqp)
    implementation(libs.spring.boot.data.jpa)
//    implementation(libs.spring.boot.data.redis)
//    implementation(libs.spring.boot.mail)
//    implementation(libs.spring.boot.security)
//    implementation(libs.spring.boot.thymeleaf)
    implementation(libs.spring.boot.web.mvc)
//    implementation(libs.spring.boot.websocket)

    implementation(libs.kotlin.reflect)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.thymeleaf.extras.springsecurity6)

    runtimeOnly(libs.postgresql)

    testImplementation(libs.spring.boot.actuator.test)
    testImplementation(libs.spring.boot.amqp.test)
    testImplementation(libs.spring.boot.data.jpa.test)
    testImplementation(libs.spring.boot.data.redis.test)
    testImplementation(libs.spring.boot.mail.test)
    testImplementation(libs.spring.boot.security.test)
    testImplementation(libs.spring.boot.thymeleaf.test)
    testImplementation(libs.spring.boot.web.mvc.test)
    testImplementation(libs.spring.boot.websocket.test)

    testImplementation(libs.kotlin.test.junit5)

    testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xannotation-default-target=param-property"
        )
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}