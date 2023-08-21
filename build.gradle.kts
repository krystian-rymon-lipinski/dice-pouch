// Top-level build file where you can add configuration options common to all sub-projects/modules.

ext {
    /* Plugins block does not work with variables. Every hilt version update must be reflected there as well. */
    set("hiltVersion", "2.46")
}

plugins {
    val agpVersion = "8.1.0"
    val kotlinVersion = "1.8.10"

    id("com.android.application") version agpVersion apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version kotlinVersion apply false
    id("org.jetbrains.kotlin.kapt") version kotlinVersion apply false
    id("com.google.dagger.hilt.android") version "2.46" apply false
}