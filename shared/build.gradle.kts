import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("app.cash.sqldelight") version "2.1.0"
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:runtime:2.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
                implementation("app.cash.sqldelight:coroutines-extensions:2.1.0")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:android-driver:2.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

                // Hilt
                implementation("com.google.dagger:hilt-android:2.51.1")

                // ViewModel
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
                implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
        }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        iosMain.dependencies {
            implementation("app.cash.sqldelight:native-driver:2.1.0")
        }
    }
}

sqldelight {
    databases {
        create("TaskDatabase") {
            packageName.set("com.aimon.app.taskmanager.shared.database")
            srcDirs.setFrom("src/commonMain/sqldelight")
        }
    }
}

android {
    namespace = "com.aimon.app.taskmanager.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

// KSP configuration for Hilt
dependencies {
    add("kspAndroid", "com.google.dagger:hilt-android-compiler:2.51.1")
}