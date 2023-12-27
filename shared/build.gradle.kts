plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("native.cocoapods")
    id("kotlinx-serialization")
    id("io.realm.kotlin")
    id("com.google.devtools.ksp")
    id("com.rickclephas.kmp.nativecoroutines")
}

version = "1.0"

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "shared module contains business logic for the convenire app"
        homepage = "Link to the Shared Module homepage"
        podfile = project.file("../iosApp/Podfile")
        ios.deploymentTarget = "14.1"
        framework { baseName = "shared" }
        pod(name = "libPhoneNumber-iOS") {
            moduleName = "libPhoneNumber_iOS"
            source = git("https://github.com/iziz/libPhoneNumber-iOS")
        }
        pod("KMPNativeCoroutinesAsync", Versions.kmpNativeCoroutinesVersion)
    }

    sourceSets {
        this.all { languageSettings.optIn("kotlin.experimental.ExperimentalObjCName") }

        val commonMain by getting {
            dependencies {
                with(Deps.Ktor) {
                    implementation(clientCore)
                    implementation(clientJson)
                    implementation(clientLogging)
                    implementation(contentNegotiation)
                    implementation(auth)
                    implementation(json)
                }

                with(Deps.Realm) {
                    implementation(libraryBase)
                }

                with(Deps.Kotlinx) {
                    implementation(coroutinesCore)
                    implementation(serializationCore)
                    implementation(serializationJson)
                    api(dateTimeKtx)
                }

                with(Deps.MultiplatformSettings) {
                    implementation(core)
                }

                with(Deps.Koin) {
                    api(core)
                    api(test)
                }
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                with(Deps.Ktor) {
                    implementation(clientAndroid)
                }
                implementation("com.googlecode.libphonenumber:libphonenumber:8.12.32")
                implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(Deps.Ktor.clientDarwin)
            }
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.tomorrow.convenire.shared"
    compileSdk = Versions.androidTargetSdk
    defaultConfig {
        minSdk = Versions.androidMinSdk
    }
}
dependencies {
    implementation("androidx.compose.ui:ui-text:1.3.3")
}
