import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// The sample needs your Firebase config to receive pushes; without it, it still builds.
if (file("google-services.json").exists()) apply(plugin = "com.google.gms.google-services")

// Your PushGenius app ID (and optionally a host) come from local.properties, which isn't committed:
//   pushgenius.appId=...
//   pushgenius.host=http://192.168.1.10:3001
val local = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}

android {
    namespace = "us.pushgeni.sample"
    compileSdk = 35
    defaultConfig {
        applicationId = local.getProperty("pushgenius.applicationId") ?: "us.pushgeni.sample"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.2.0"
        buildConfigField("String", "PUSHGENIUS_APP_ID", "\"${local.getProperty("pushgenius.appId", "")}\"")
        buildConfigField("String", "PUSHGENIUS_HOST", "\"${local.getProperty("pushgenius.host", "")}\"")
    }
    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(project(":pushgenius"))
}
