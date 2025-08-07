# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

##################################
# Android + Kotlin + Compose
##################################

# Kotlin
-dontwarn kotlin.**
-keep class kotlin.** { *; }

# Jetpack Compose
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# Keep Compose preview annotations (optional)
-keep @androidx.compose.ui.tooling.preview.Preview class * { *; }

##################################
# Coroutines
##################################
-dontwarn kotlinx.coroutines.**
-keep class kotlinx.coroutines.** { *; }

##################################
# Retrofit
##################################
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**

-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

##################################
# Gson
##################################
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.JsonDeserializer
-keep class * implements com.google.gson.JsonSerializer

# Optional: Keep model classes (if used in deserialization)
-keep class com.example.weatherapp.model.** { *; }

##################################
# Hilt / Dagger
##################################
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.hilt.** { *; }

-keepclassmembers class * {
    @dagger.hilt.android.HiltAndroidApp <init>(...);
}
-keepclassmembers class * {
    @dagger.hilt.android.lifecycle.HiltViewModel <init>(...);
}

##################################
# Android
##################################
# Prevent obfuscation of Activity, Fragment, and ViewModel
-keep public class * extends android.app.Activity
-keep public class * extends androidx.fragment.app.Fragment
-keep class * extends androidx.lifecycle.ViewModel { *; }

# Keep Application class
-keep class com.oam.weatherapp.WeatherAppApplication { *; }

# Prevent warnings about generated classes
-dontwarn com.example.weatherapp.**

##################################
# General Best Practices
##################################

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Don't obfuscate enums (if you use them)
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
