plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    id("org.jetbrains.kotlin.kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.dk.doodlekong"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dk.doodlekong"
        minSdk = 24
        targetSdk = 35
        versionCode = 20250525
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    defaultConfig {
        versionCode = 20250525
        versionName = "1.0.3"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Lottie
    implementation(libs.lottie)
    
    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Easy Permissions
    implementation(libs.easypermissions)

    // Retrofit
    implementation(libs.com.squareup.retrofit2.retrofit5)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)

    // Activity KTX for viewModels()
    implementation(libs.androidx.activity.ktx)

    // Timber
    implementation(libs.timber)

    // Architectural Components
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Lifecycle
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx.v287)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v287)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx.v289)
    implementation(libs.androidx.navigation.ui.ktx.v289)

    // Gson
    implementation(libs.gson)


    //Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
}