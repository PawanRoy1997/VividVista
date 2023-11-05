package config

import org.gradle.api.JavaVersion

object ProjectConfig {
    object Plugins {
        object Ids {
            const val kotlin = "org.jetbrains.kotlin.android"
        }
    }

    object Android {
        const val namespace = "com.nextxform.vividvista"
        const val compileSdk = 34

        object DefaultConfig {
            const val applicationId = "com.nextxform.vividvista"
            const val minSdk = 24
            const val targetSdk = 33
            const val versionCode = 1
            const val versionName = "1.0"

            const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            object VectorDrawables {
                const val useSupportLibrary = true
            }
        }

        object BuildTypes {
            object Release {
                const val isMinifyEnabled = true
                const val defaultProguardOptimize = "proguard-android-optimize.txt"
                const val proguardRule = "proguard-rules.pro"
            }
        }

        object CompileOptions {
            val sourceCompatibility = JavaVersion.VERSION_17
            val targetCompatibility = JavaVersion.VERSION_17
        }

        object KotlinOptions {
            const val jvmTarget = "17"
        }

        object BuildFeatures {
            const val compose = true
        }

        object ComposeOptions {
            val kotlinCompilerExtensionVersion = "1.4.3"
        }

        object Packaging {
            object Resources {
                val excludes = arrayOf("/META-INF/{AL2.0,LGPL2.1}")
            }
        }
    }
}