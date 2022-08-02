import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.6.10"
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            export("com.badoo.reaktive:reaktive:1.2.2")
            export("com.arkivanov.essenty:lifecycle:0.4.1")
            export("com.arkivanov.essenty:instance-keeper:0.4.1")
            export("com.arkivanov.mvikotlin:mvikotlin:3.0.0-beta02")
            export("com.arkivanov.mvikotlin:mvikotlin-main:3.0.0-beta02")
            export("com.arkivanov.mvikotlin:mvikotlin-logging:3.0.0-beta02")
        }
    }
    
    sourceSets {
        val ktorVersion = "2.0.3"

        val commonMain by getting {
            dependencies {
                api("com.badoo.reaktive:reaktive:1.2.2")
                api("com.arkivanov.mvikotlin:mvikotlin:3.0.0-beta02")
                api("com.arkivanov.mvikotlin:mvikotlin-main:3.0.0-beta02")
                api("com.arkivanov.mvikotlin:mvikotlin-logging:3.0.0-beta02")
                api("com.arkivanov.essenty:lifecycle:0.4.1")
                api("com.arkivanov.essenty:instance-keeper:0.4.1")
                implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-reaktive:3.0.0-beta02")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 32
    }
}
