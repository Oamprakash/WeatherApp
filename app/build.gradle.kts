import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

//val apiKey by lazy {
//    project.findProperty("OPEN_WEATHER_API_KEY") as String
//}
// Create a new Properties object
val localProperties = Properties()

// Get the root project's directory
val localPropertiesFile = rootProject.file("local.properties") // or project.file("local.properties") if in project-level build.gradle and you want it specific to that project. For Android app modules, rootProject.file() is common.

// Check if the local.properties file exists
if (localPropertiesFile.exists()) {
    // Load the properties from the file
    localPropertiesFile.inputStream().use { input ->
        localProperties.load(input)
    }
} else {
    // Optionally, handle the case where the file doesn't exist (e.g., provide defaults or throw an error)
    // For CI environments, you might set these via environment variables instead [1]
    println("Warning: local.properties file not found. Using default values or environment variables if available.")
}

// Access the properties by their key
val apiKey: String? = localProperties.getProperty("OPEN_WEATHER_API_KEY")


android {
    namespace = "com.oam.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.oam.weatherapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

//        buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"$apiKey\"")
        buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"${apiKey ?: "DEFAULT_API_KEY_IF_MISSING"}\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Hilt (if used)
    implementation("com.google.dagger:hilt-android:2.52")
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
//    implementation(libs.lifecycle.viewmodel.compose)

    // Logging (optional for debugging)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Location (if needed)
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Room
    implementation(libs.androidx.room.runtime)
    kapt("androidx.room:room-compiler:2.7.2")
    implementation(libs.androidx.room.ktx) // for coroutines/Flow support

    //Work manager
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    implementation ("androidx.hilt:hilt-work:1.2.0")
    kapt ("androidx.hilt:hilt-compiler:1.2.0")

    //SwipeToRefresh
//    implementation ("com.google.accompanist:accompanist-swiperefresh:0.36.0")
    implementation ("androidx.compose.material:material:1.4.0")

    //Location permission
    implementation ("com.google.accompanist:accompanist-permissions:0.35.0-alpha")
    implementation ("com.google.android.gms:play-services-location:21.0.1")


    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.4")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.4")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.4")
}