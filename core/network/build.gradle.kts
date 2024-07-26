import com.karangajjar.cryptolist.buildSrc.ProjectConfig

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.karangajjar.cryptolist.core.network"
    compileSdk = ProjectConfig.compileSdk
    defaultConfig {
        minSdk = ProjectConfig.minSdk
    }
    kotlinOptions {
        jvmTarget = ProjectConfig.kotlinJvmTarget
    }
    compileOptions {
        sourceCompatibility = ProjectConfig.javaCompatibilityVersion
        targetCompatibility = ProjectConfig.javaCompatibilityVersion
    }
    packaging {
        resources {
            excludes += listOf(
                "META-INF/gradle/incremental.annotation.processors"
            )
        }
    }
}
dependencies  {
    implementation(project(":core:model"))
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson.converter)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.core.ktx)
    implementation(project(":core:common"))
}
