package com.karangajjar.cryptolist.buildSrc

import org.gradle.api.JavaVersion

object ProjectConfig {
    const val compileSdk = 34
    const val minSdk = 24
    const val targetSdk = 34

    const val versionCode = 1
    const val versionName = "1.0"

    const val kotlinJvmTarget = "1.8"
    val javaCompatibilityVersion = JavaVersion.VERSION_1_8
}