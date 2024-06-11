import java.io.File
import java.nio.file.Paths

plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.4.0").apply(false)
    id("com.android.library").version("7.4.0").apply(false)
    kotlin("android").version("1.9.23").apply(false)
    kotlin("multiplatform").version("1.9.23").apply(false)
    id("com.google.devtools.ksp").version("1.9.23-1.0.20").apply(false)
    id("com.rickclephas.kmp.nativecoroutines").version(Versions.kmpNativeCoroutinesVersion)
        .apply(false)
}

buildscript {
    val kotlinVersion: String by project

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        // keeping this here to allow automatic updates
        classpath("com.android.tools.build:gradle:8.3.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.7")
        classpath("com.rickclephas.kmp:kmp-nativecoroutines-gradle-plugin:${Versions.kmpNativeCoroutinesVersion}")

        with(Deps.Gradle) { classpath(realm) }
        with(Deps.Android) { classpath(googleServices) }
    }
}

fun loadEnv(filePath: String): Map<String, String> {
    val envMap = mutableMapOf<String, String>()
    val envFile = Paths.get(filePath).toFile()
    if (envFile.exists()) {
        envFile.forEachLine { line ->
            if (line.contains("=") && !line.startsWith("#")) {
                val (key, value) = line.split("=", limit = 2)
                envMap[key.trim()] = value.trim()
            }
        }
    }
    return envMap
}

allprojects {
    repositories {
        google()
        mavenCentral()
        //read env file
        val envVal = loadEnv("${project.rootProject.projectDir}/.env")
        if (!envVal["USERNAME"].isNullOrBlank() && !envVal["TOKEN"].isNullOrBlank()) {
            listOf("Android-Project-Startup", "Android-UI-Components", "kmm-Project-Startup").map {
                maven {
                    name = it
                    url = uri("https://maven.pkg.github.com/tomorrw/$it")
                    credentials {
                        username = envVal["USERNAME"]
                        password = envVal["TOKEN"]
                    }
                }
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
