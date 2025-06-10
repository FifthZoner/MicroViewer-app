plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.fz.microviewerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fz.microviewerapp"
        minSdk = 29
        targetSdk = 35
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

repositories {

}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment.ktx)
    val camerax_version ="1.2.2"
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-video:${camerax_version}")

    implementation ("androidx.camera:camera-view:${camerax_version}")
    implementation ("androidx.camera:camera-extensions:${camerax_version}")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //val camerax_version = "1.5.0-beta01"
    //// The following line is optional, as the core library is included indirectly by camera-camera2
    //implementation("androidx.camera:camera-core:${camerax_version}")
    //implementation("androidx.camera:camera-camera2:${camerax_version}")
    //// If you want to additionally use the CameraX Lifecycle library
    //implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    //// If you want to additionally use the CameraX VideoCapture library
    //implementation("androidx.camera:camera-video:${camerax_version}")
    //// If you want to additionally use the CameraX View class
    //implementation("androidx.camera:camera-view:${camerax_version}")
    //// If you want to additionally add CameraX ML Kit Vision Integration
    //implementation("androidx.camera:camera-mlkit-vision:${camerax_version}")
    //// If you want to additionally use the CameraX Extensions library
    //implementation("androidx.camera:camera-extensions:${camerax_version}")
}