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

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // LibGDX Modules
    implementation("com.badlogicgames.gdx:gdx:1.10.0")
    implementation("com.badlogicgames.gdx:gdx-box2d:1.10.0")

    // LibKTX Modules
    implementation("io.github.libktx:ktx-box2d:1.10.0-rc1")
}