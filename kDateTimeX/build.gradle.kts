import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "KDateTimeX"
            isStatic = true
        }
    }

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization)
            implementation(libs.koin.core)
            implementation(libs.logback.classic)
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test")) // Kotlin Test framework for common tests
                implementation(libs.kotlinx.coroutines.test) // For testing coroutines
            }
        }
//
//        // JVM-specific source set
//        val jvmTest by getting
//
//        // iOS-specific source sets
//        val iosX64Test by getting
//        val iosArm64Test by getting
//        val iosSimulatorArm64Test by getting
//
//        // JS-specific source set
//        val jsTest by getting
//
//        // Set dependencies for platform-specific tests if needed
//        jvmTest.dependencies {
//            implementation(kotlin("test-junit"))
//        }
//
//        jsTest.dependencies {
//            implementation(kotlin("test-js"))
//        }
//
//        // iOS tests can use the commonTest dependencies
//        iosX64Test.dependencies {
//            implementation(kotlin("test"))
//        }
//        iosArm64Test.dependencies {
//            implementation(kotlin("test"))
//        }
//        iosSimulatorArm64Test.dependencies {
//            implementation(kotlin("test"))
//        }
    }
}

android {
    namespace = "dev.sunnat629.libs.kDateTimeX"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
