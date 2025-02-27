plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    kotlin("android")
}

android {
    namespace = "com.tomorrow.mobile_starter_app"
    compileSdk = Versions.androidCompileSdk
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = Versions.composeCompiler }
    // val appVersionCode = System.getenv()["NEW_BUILD_NUMBER"]?.toInt() ?: 8
    val appVersionCode = 13

    signingConfigs {
        create("release") {
            if (System.getenv()["CI"] == "true") { // CI=true is exported by Codemagic
                storeFile = System.getenv()["FCI_KEYSTORE_PATH"]?.let { file(it) }
                storePassword = System.getenv()["CM_KEYSTORE_PASSWORD"]
                keyAlias = System.getenv()["CM_KEY_ALIAS_USERNAME"]
                keyPassword = System.getenv()["CM_KEY_ALIAS_PASSWORD"]
            } else {
                storeFile = file("release-key.keystore")
                storePassword = "password"
                keyAlias = "aliasKey"
                keyPassword = "password"
            }
        }
    }

    defaultConfig {
        applicationId = "com.tomorrow.convenire"
        minSdk = Versions.androidMinSdk
        targetSdk = Versions.androidTargetSdk
        compileSdk = Versions.androidCompileSdk
        versionCode = appVersionCode
        versionName = Versions.applicationVersionName
        signingConfig = signingConfigs.getByName("release")
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":shared"))

    with(Deps.Compose) {
        implementation(ui)
        implementation(foundation)
        implementation(material)
        implementation(material3)
        implementation(navigationCompose)
        implementation(coilCompose)
        implementation(viewModel)
        implementation(navigationRunTime)
    }
    with(Deps.Android) {
        implementation(googlePlayUpdate)
        implementation(googlePlayUpdateKtx)
        implementation(systemUiController)
    }
    with(Deps.AndroidX) {
        implementation(splashScreen)
        implementation(activityCompose)
    }
    with(Deps.Koin) {
        implementation(core)
        implementation(android)
        implementation(compose)
    }
    with(Deps.FireBase) {
        implementation(bom)
        implementation(messaging)
        implementation(crashlytics)
        implementation(analytics)
    }
    implementation("com.tomorrow.kmmProjectStartup:shared-android:1.0.0")
    implementation(kotlin("reflect"))

    //TOMORROW'S DEPENDENCIES
    with(Deps.Tomorrow.UIComponents) {
        implementation(Components)
        implementation(EventListing)
        implementation(ListDisplay)
        implementation(Carousel)
        implementation(QrCode)
        implementation(VideoPlayer)
    }

    with(Deps.Tomorrow.ProjectStartup) {
        implementation(InternetConnectivity)
        implementation(Navigation)
        implementation(AppUpdate)
        implementation(ReadViewModel)
        implementation(RequestPermission)
    }
}