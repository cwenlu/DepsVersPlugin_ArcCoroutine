import com.cwl.depsversplugin.*
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions") //废弃
}

android {
    compileSdkVersion(BuildConfig.compileSdkVersion)
    buildToolsVersion(BuildConfig.buildToolsVersion)


    defaultConfig {
        minSdkVersion(BuildConfig.minSdkVersion)
        targetSdkVersion(BuildConfig.targetSdkVersion)
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

    }

    androidExtensions { isExperimental = true }
    buildFeatures{
        viewBinding=true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

generalCommonDeps(KotlinCompilerVersion.VERSION)

api(
    Coroutines.kotlinx_coroutines_core,
    Json.moshi_kotlin,

    Okttp.PersistentCookieJar,
    Okttp.okhttp3_logging_interceptor,

    HelperUtil.timber,
    Deps.appcompat,

    Lifecycle.lifecycle_viewmodel_ktx,
    Lifecycle.lifecycle_common_java8,
    Lifecycle.lifecycle_livedata_ktx,

    Widget.RecyclerView.recyclerview,
    Widget.RecyclerView.recyclerview_selection,
    Widget.viewpager2,
    Koin.koin_core

)

cmpOnly(
    Image.picasso,
    Widget.paging_runtime
)


