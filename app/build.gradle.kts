plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val signingStorePassword = System.getenv("NIVORANT_STORE_PASSWORD")
val signingKeyPassword = System.getenv("NIVORANT_KEY_PASSWORD")
    ?: signingStorePassword

android {
    namespace = "com.dmvpn.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dmvpn.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        create("release") {
            storeFile = file("nivorant-upload.jks")
            storePassword = signingStorePassword
            keyAlias = "nivorant"
            keyPassword = signingKeyPassword
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
