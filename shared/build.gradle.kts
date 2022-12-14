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

    val mvikotlinVersion = "3.0.0-beta02"
    val essentryVersion = "0.4.1"
    val ktorVersion = "2.0.3"

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            export("com.arkivanov.mvikotlin:mvikotlin:$mvikotlinVersion")
            export("com.arkivanov.mvikotlin:mvikotlin-main:$mvikotlinVersion")
            export("com.arkivanov.mvikotlin:mvikotlin-logging:$mvikotlinVersion")
            export("com.arkivanov.essenty:lifecycle:$essentryVersion")
            export("com.arkivanov.essenty:instance-keeper:$essentryVersion")
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("com.arkivanov.mvikotlin:mvikotlin:$mvikotlinVersion")
                api("com.arkivanov.mvikotlin:mvikotlin-main:$mvikotlinVersion")
                api("com.arkivanov.mvikotlin:mvikotlin-logging:$mvikotlinVersion")
                api("com.arkivanov.essenty:lifecycle:$essentryVersion")
                api("com.arkivanov.essenty:instance-keeper:$essentryVersion")
                implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:${mvikotlinVersion}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
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
