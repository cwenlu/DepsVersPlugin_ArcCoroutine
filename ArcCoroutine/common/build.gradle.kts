import com.cwl.depsversplugin.*
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.0")


    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

    }

    androidExtensions { isExperimental = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

projectGeneralDeps(KotlinCompilerVersion.VERSION)

api(
    Coroutines.kotlinx_coroutines_core,
    Json.moshi_kotlin,

    Okttp.PersistentCookieJar,
    Okttp.okhttp3_logging_interceptor,

    Deps.timber,
    Deps.appcompat,

    Lifecycle.lifecycle_viewmodel_ktx,
    Lifecycle.lifecycle_common_java8,
    Lifecycle.lifecycle_livedata_ktx,

    Recyclerview.recyclerview,
    Recyclerview.recyclerview_selection,

    Koin.koin_core

)

cmpOnly(
    Image.picasso
)

