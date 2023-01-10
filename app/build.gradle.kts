import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    alias(libraries.plugins.gradle.ktlint)
    alias(libraries.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.accu.cityweather"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.accu.cityweather"
        minSdk = 21
        targetSdk = 33
        versionCode = 2
        versionName = ".2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes.forEach {
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
        this.forEach {
            it.buildConfigField(
                "String",
                "WEATHER_API_URL",
                "\"https://api.openweathermap.org/\""
            )
            it.buildConfigField(
                "String",
                "WEATHER_ICON_URL",
                "\"https://openweathermap.org/img/wn/\""
            )
            it.buildConfigField(
                "String",
                "WEATHER_API_APP_ID",
                gradleLocalProperties(rootDir).getProperty("weather.appid")
            )
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    sourceSets {
        val kotlinAdditionalSourceSets = project.file("src/main/kotlin")
        findByName("main")?.java?.srcDirs(kotlinAdditionalSourceSets)
    }
}

dependencies {

    implementation(libraries.androidx.core.ktx)
    implementation(libraries.androidx.lifecycle.runtime.ktx)
    implementation(libraries.bundles.composeUi)
    implementation(libraries.bundles.koin)
    implementation(libraries.bundles.androidLocation)
    implementation(libraries.kotlinx.serialization)
    implementation(libraries.bundles.retrofit)
    implementation(libraries.androidx.work)
    implementation(libraries.easypermissions.ktx)
    testImplementation(testLibraries.junit)
    androidTestImplementation(testLibraries.androidx.test.ext.junit)
    androidTestImplementation(testLibraries.bundles.androidUiTesting)
    debugImplementation(libraries.bundles.composeDebug)
}
