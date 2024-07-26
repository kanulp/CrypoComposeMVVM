import com.karangajjar.cryptolist.buildSrc.ProjectConfig

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.karangajjar.cryptolist.core.model"
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
    implementation(libs.core.ktx)
}
