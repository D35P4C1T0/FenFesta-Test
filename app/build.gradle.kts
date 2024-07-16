plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.fenfesta"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fenfesta"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {

        // Enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

var composeVersion = "1.6.8"
var mapboxVersion = "11.3.1"

dependencies {

    // Notifications
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.core:core-ktx:1.10.1")

    // GPS
    implementation ("com.google.android.gms:play-services-ads:23.1.0")
    implementation("com.google.android.gms:play-services-ads:23.1.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Add Coil for image loading
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Add Moshi for JSON parsing
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi)
    implementation(libs.moshi.converter)

    // Add Retrofit for network requests
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.material.icons.extended.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation("com.mapbox.maps:android:$mapboxVersion")
    // If you're using compose also add the compose extension
    implementation("com.mapbox.extension:maps-compose:$mapboxVersion")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.mapbox.plugin:maps-annotation:$mapboxVersion")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    // The compose calendar library
    implementation("com.kizitonwose.calendar:compose:2.6.0-beta02")
    implementation("androidx.compose.animation:animation:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // http logging
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // dynamic status bar
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
}