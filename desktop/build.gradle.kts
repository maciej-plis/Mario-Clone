import org.gradle.api.JavaVersion.VERSION_17

plugins {
    kotlin("jvm").version("1.6.20")
    java
}

group = rootProject.group
version = rootProject.version

java {
    sourceCompatibility = VERSION_17
    targetCompatibility = VERSION_17
}

sourceSets.main {
    java.srcDirs("src/")
    resources.srcDirs("../assets")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.10.0")
    implementation("com.badlogicgames.gdx:gdx-platform:1.10.0:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:1.10.0:natives-desktop")

    implementation(project(":core"))
}