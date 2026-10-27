plugins {
    id("com.android.application")
}

android {
    namespace = "com.defold.example.basic3d"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.defold.example.basic3d"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        ndk {
            abiFilters += "arm64-v8a"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
    }

    androidResources {
        noCompress += listOf("arcd", "arci", "dmanifest", "projectc", "vkq")
    }
}
