import org.jetbrains.kotlin.gradle.plugin.extraProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
}

android {
    signingConfigs {
        create("release") {
            val keystorePropertiesFile: File = rootProject.file("keystore.properties")
            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))

            storeFile = file(keystoreProperties["keystoreFile"].toString())
            keyAlias = keystoreProperties["keyAlias"].toString()
            storePassword = keystoreProperties["keystorePassword"].toString()
            keyPassword = keystoreProperties["keyPassword"].toString()
        }
    }
    namespace = "com.krystianrymonlipinski.dicepouch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.krystianrymonlipinski.dicepouch"
        minSdk = 26
        targetSdk = 36
        versionCode = 5
        versionName = "1.0.2"

        testInstrumentationRunner = "com.krystianrymonlipinski.dicepouch.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            applicationIdSuffix = ".release"
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2023.06.01")
    val hiltVersion = rootProject.extraProperties.properties["hiltVersion"] as String
    val composeNavigationVersion = "2.7.5"
    val roomVersion = "2.6.1"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.navigation:navigation-compose:$composeNavigationVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.google.android.material:material:1.10.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.mockito:mockito-inline:4.11.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    //noinspection GradleDependency; 2.7.2 needs API 34
    androidTestImplementation("androidx.navigation:navigation-testing:$composeNavigationVersion")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")

    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation("com.github.skydoves:colorpicker-compose:1.0.5")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KaptGenerateStubs> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kapt {
    correctErrorTypes = true
    arguments {
        arg("resourcePackageName", android.defaultConfig.applicationId ?: "com.krystianrymonlipinski.dicepouch")
    }
}