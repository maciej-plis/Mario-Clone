import org.gradle.api.JavaVersion.VERSION_17

plugins {
    kotlin("jvm").version("1.6.20")
    java
}

group = rootProject.group
version = rootProject.version

sourceSets.main {
    java.srcDirs("src/")
}

java {
    sourceCompatibility = VERSION_17
    targetCompatibility = VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.badlogicgames.gdx:gdx:1.10.0")
    implementation("com.badlogicgames.gdx:gdx-box2d:1.10.0")
}