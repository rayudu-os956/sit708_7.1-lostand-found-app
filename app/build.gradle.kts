plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.lostfoundapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lostfoundapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}


dependencies {
    // AndroidX + Kotlin
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")

    implementation("com.google.android.libraries.places:places:2.8.0")

    // Maps & Places & Location
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.libraries.places:places:4.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
}