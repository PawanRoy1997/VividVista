import config.ProjectConfig.Android
import config.ProjectConfig.Android.BuildTypes.Release
import config.ProjectConfig.Android.CompileOptions
import config.ProjectConfig.Android.ComposeOptions
import config.ProjectConfig.Android.DefaultConfig
import config.ProjectConfig.Android.Packaging.Resources

plugins {
    alias(libs.plugins.appliation)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
}

android {
    namespace = Android.namespace
    compileSdk = Android.compileSdk

    defaultConfig {
        applicationId = DefaultConfig.applicationId
        minSdk = DefaultConfig.minSdk
        targetSdk = DefaultConfig.targetSdk
        versionCode = DefaultConfig.versionCode
        versionName = DefaultConfig.versionName

        testInstrumentationRunner = DefaultConfig.testInstrumentationRunner
        vectorDrawables {
            useSupportLibrary = DefaultConfig.VectorDrawables.useSupportLibrary
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = Release.isMinifyEnabled
            proguardFiles(
                getDefaultProguardFile(Release.defaultProguardOptimize),
                Release.proguardRule
            )
        }
    }
    compileOptions {
        sourceCompatibility = CompileOptions.sourceCompatibility
        targetCompatibility = CompileOptions.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = Android.KotlinOptions.jvmTarget
    }
    buildFeatures {
        compose = Android.BuildFeatures.compose
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ComposeOptions.kotlinCompilerExtensionVersion
    }
    packaging {
        resources {
            excludes += Resources.excludes
        }
    }
}

dependencies {
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.activity)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.material3)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)

    // Unit Testing
    testImplementation(libs.junit)

    // Instrumentation
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
    androidTestImplementation(libs.compose.junit)

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.manifest)
}