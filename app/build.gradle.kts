plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.nlm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nlm"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isShrinkResources = false
            isDebuggable = true
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
    dataBinding{
        enable = true
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Match your Compose version
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.runtime.saved.instance.state)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.preference.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(platform(libs.androidx.compose.bom))
    //gson
    implementation(libs.gson)

    //retrofit 2
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation (libs.retrofit2.adapter.rxjava2)
    implementation(libs.retrofit)

    //circle image view
    implementation(libs.circleimageview)

    //Glide
    implementation(libs.glide)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.jetbrains.kotlinx.serialization.core)

    //viewpager 2
    implementation(libs.androidx.viewpager2)

    implementation (libs.picasso)

    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //rxkotlin
    implementation (libs.rxkotlin)
    implementation (libs.rxandroid)

    implementation(libs.androidx.viewpager2)

    //swipe for refresh
    implementation(libs.androidx.swiperefreshlayout)
    implementation ("com.google.android.gms:play-services-location:21.0.1")



    //biometric
    implementation(libs.androidx.biometric)

    implementation (libs.androidx.activity.compose )// Use the latest version
    implementation (libs.androidx.ui   )         // Latest Compose version

    implementation("com.vanniktech:android-image-cropper:4.6.0")



}