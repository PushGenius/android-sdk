plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}

android {
    namespace = "us.pushgeni.sdk"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    publishing {
        singleVariant("release") { withSourcesJar() }
    }
}

dependencies {
    implementation("com.google.firebase:firebase-messaging:24.1.1")
    implementation("org.json:json:20240303")
}

// JitPack builds a git tag and passes it as -Pversion; local builds fall back to 0.2.0.
val sdkVersion = (findProperty("version") as String?)?.takeIf { it != "unspecified" } ?: "0.2.0"

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "us.pushgeni"
            artifactId = "pushgenius-android"
            version = sdkVersion
            afterEvaluate { from(components["release"]) }
        }
    }
}
