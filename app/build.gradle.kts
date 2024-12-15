import org.gradle.kotlin.dsl.internal.sharedruntime.codegen.pluginEntriesFrom

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)

    kotlin(libs.plugins.data.kotlinSerialization.get().pluginId) version libs.versions.kotlin
}

android {
    namespace = "com.github.xe11.rvrside"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.github.xe11.rvrside"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"

        freeCompilerArgs += listOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)

    implementation(libs.network.okhttp)
    implementation(libs.network.okhttpLogger)
    implementation(libs.network.retrofit)
    implementation(libs.network.retrofitKotlinSerialization)
    implementation(libs.data.kotlinSerialization)
    implementation(libs.ui.paging)
    implementation(libs.ui.coilImageLoader)
    implementation(libs.ui.coilImageLoaderOkhttp)

    implementation(libs.dagger.dagger)
    kapt(libs.dagger.compiler)
}
