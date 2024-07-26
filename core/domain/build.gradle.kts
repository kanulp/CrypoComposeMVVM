import com.karangajjar.cryptolist.buildSrc.ProjectConfig

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.hilt)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)

}

android {
    namespace = "com.karangajjar.cryptolist.core.domain"
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
dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(project(":core:model"))
    implementation(libs.core.ktx)
}