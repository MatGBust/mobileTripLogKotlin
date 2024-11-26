import java.util.Properties

// Load secrets.properties file
val secretPropertiesFile = rootProject.file("secrets.properties")
val secretProperties = Properties()

if (secretPropertiesFile.exists()) {
    secretProperties.load(secretPropertiesFile.inputStream())
}

buildscript {
    dependencies {
        classpath(libs.secrets.gradle.plugin)
    }
}


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.secrets.gradle)
    kotlin("kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.triplogger"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.triplogger"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["MAP_API_KEY"] = secretProperties["MAP_API_KEY"] as String


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures{
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            buildConfigField("String", "MAP_API_KEY", "\"${secretProperties.getProperty("MAP_API_KEY")}\"")
        }
        debug {
            buildConfigField("String", "MAP_API_KEY", "\"${secretProperties.getProperty("MAP_API_KEY")}\"")
        }
    }
    compileOptions {
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation(libs.play.services.maps)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.libraries.places:places:2.4.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("org.mockito:mockito-inline:4.8.0")
    testImplementation ("org.mockito:mockito-core:4.8.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.robolectric:robolectric:4.9")
    testImplementation ("org.robolectric:shadows-framework:4.10.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")

}