object Versions {
    const val androidMinSdk = 26
    const val androidCompileSdk = 34
    const val androidTargetSdk = androidCompileSdk
    const val applicationVersionName = "1.1.22"

    const val kotlinCoroutines = "1.6.4"
    const val koin = "3.4.3"
    const val ktor = "2.3.2"

    const val realm = "1.7.1"

    const val kotlinxSerialization = "1.5.1"

    const val kmpNativeCoroutinesVersion = "1.0.0-ALPHA-4"

    const val compose = "1.5.3"
    const val composeCompiler = "1.5.3"
    const val accompanist = "0.30.1"

    const val material3 = "1.1.1"
    const val material = "1.4.3"
    const val activityCompose = "1.7.2"
    const val lifecycleKtx = "2.6.1"
    const val lifecycleRuntimeKtx = lifecycleKtx
    const val lifecycleViewmodelKtx = lifecycleKtx

    const val navigation = "2.5.3"
    const val multiplatformSettings = "1.0.0"
}

object Deps {
    object MultiplatformSettings {
        const val core = "com.russhwolf:multiplatform-settings:${Versions.multiplatformSettings}"
    }

    object Gradle {
        const val realm = "io.realm.kotlin:gradle-plugin:${Versions.realm}"
    }

    object Kotlinx {
        const val serializationCore =
            "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerialization}"
        const val serializationJson =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}"
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
        const val coroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}"
        const val dateTimeKtx = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"
    }

    object Android {
        const val material = "com.google.android.material:material:${Versions.material}"
        const val barcodeEncodingDecoding = "com.google.zxing:core:3.5.1"
        const val systemUiController =
            "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}"
        const val permissionAccompanist =
            "com.google.accompanist:accompanist-permissions:${Versions.accompanist}"


        const val googlePlayUpdate = "com.google.android.play:app-update:2.1.0"
        const val googlePlayUpdateKtx = "com.google.android.play:app-update-ktx:2.1.0"

        const val googleServices = "com.google.gms:google-services:4.3.15"
        const val exoPlayer = "com.google.android.exoplayer:exoplayer:2.19.0"
    }


    object AndroidX {
        const val lifecycleRuntimeCompose =
            "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycleRuntimeKtx}"
        const val lifecycleRuntimeKtx =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"
        const val lifecycleViewModelKtx =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewmodelKtx}"
        const val lifecycleViewModel =
            "androidx.lifecycle:lifecycle-viewmodel:${Versions.lifecycleViewmodelKtx}"
        const val lifecycleViewModelCompose =
            "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleViewmodelKtx}"

        const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
        const val splashScreen = "androidx.core:core-splashscreen:1.0.1"
    }

    object Compose {
        const val compiler = "androidx.compose.compiler:compiler:${Versions.composeCompiler}"
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val uiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
        const val uiViewBinding = "androidx.compose.ui:ui-viewbinding:${Versions.compose}"
        const val foundationLayout =
            "androidx.compose.foundation:foundation-layout:${Versions.compose}"
        const val foundation = "androidx.compose.foundation:foundation:${Versions.compose}"
        const val material = "androidx.compose.material:material:${Versions.compose}"
        const val material3 = "androidx.compose.material3:material3:${Versions.material3}"
        const val navigationCompose =
            "androidx.navigation:navigation-compose:${Versions.navigation}"
        const val navigationRunTime =
            "androidx.navigation:navigation-runtime:${Versions.navigation}"
        const val materialIcons =
            "androidx.compose.material:material-icons-extended:${Versions.compose}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0-alpha01"
        const val coilCompose = "io.coil-kt:coil-compose:2.4.0"
    }

    object Koin {
        const val core = "io.insert-koin:koin-core:${Versions.koin}"
        const val test = "io.insert-koin:koin-test:${Versions.koin}"
        const val testJUnit4 = "io.insert-koin:koin-test-junit4:${Versions.koin}"
        const val android = "io.insert-koin:koin-android:${Versions.koin}"
        const val compose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
    }

    object Ktor {
        const val serverCors = "io.ktor:ktor-server-cors:${Versions.ktor}"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
        const val json = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
        const val auth = "io.ktor:ktor-client-auth:${Versions.ktor}"

        const val clientCore = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val clientJson = "io.ktor:ktor-client-json:${Versions.ktor}"
        const val clientLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
        const val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
        const val clientAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
        const val clientDarwin = "io.ktor:ktor-client-darwin:${Versions.ktor}"
        const val clientJs = "io.ktor:ktor-client-js:${Versions.ktor}"
    }

    object Realm {
        const val libraryBase = "io.realm.kotlin:library-base:${Versions.realm}"
    }

    object FireBase {
        const val bom = "com.google.firebase:firebase-bom:32.0.0"
        const val messaging = "com.google.firebase:firebase-messaging-ktx:23.1.2"
        const val analytics = "com.google.firebase:firebase-analytics:21.2.2"
        const val crashlytics = "com.google.firebase:firebase-crashlytics:18.3.7"
    }
}
