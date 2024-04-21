// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    // Google Auth
    id("com.google.gms.google-services") version "4.4.1" apply false
    // Hilt
    id("com.google.dagger.hilt.android") version "2.44" apply false
}