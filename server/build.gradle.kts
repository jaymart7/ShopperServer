plugins {
    application
    /*alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktor)*/
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("io.ktor.plugin") version "3.0.0-beta-2"
}

group = "ph.mart.shopper"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    /*implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.postgresql)*/

    implementation("io.ktor:ktor-server-core-jvm:3.0.0-beta-2")
    implementation("io.ktor:ktor-server-netty-jvm:3.0.0-beta-2")
    implementation("io.ktor:ktor-server-cors:3.0.0-beta-2")
    implementation("io.ktor:ktor-server-auth:3.0.0-beta-2")
    implementation("io.ktor:ktor-server-auth-jwt:3.0.0-beta-2")
    implementation("io.ktor:ktor-server-content-negotiation:3.0.0-beta-2")

    implementation("io.ktor:ktor-server-config-yaml:3.0.0-beta-2")

    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.postgresql:postgresql:42.5.1")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0-beta-2")
}