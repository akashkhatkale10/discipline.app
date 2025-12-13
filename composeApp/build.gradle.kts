import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.swiftklib)
    id("kotlin-parcelize")
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.addAll("-P", "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=cl.emilym.kmp.parcelable.Parcelize")
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts.add("-lsqlite3")
        }

        iosTarget.compilations {
            val main by getting {
                cinterops {
                    create("appblocker")
                }
            }
        }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.material)
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:33.13.0"))

            implementation ("androidx.credentials:credentials:1.0.0-alpha05")
            implementation ("androidx.credentials:credentials-play-services-auth:1.5.0")
            implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.1")

            implementation("androidx.datastore:datastore-preferences:1.1.1")
        }
        commonMain.dependencies {
            implementation("com.eygraber:uri-kmp:0.0.19")
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha12")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

            implementation("cl.emilym.kmp:parcelable:0.1.2")

            // google
            implementation("io.github.mirzemehdi:kmpauth-google:2.3.1") //Google One Tap Sign-In
            implementation("io.github.mirzemehdi:kmpauth-firebase:2.3.1") //Integrated Authentications with Firebase
            implementation("io.github.mirzemehdi:kmpauth-uihelper:2.3.1")

            // viewmodel
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
            implementation(libs.androidx.lifecycle.viewmodel)

            // koin
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)

            // date time
            implementation(libs.kotlinx.datetime)

            // room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            // firebase
            val version = "2.4.0"
            implementation("dev.gitlive:firebase-firestore:$version") // This line
            implementation("dev.gitlive:firebase-auth:$version") // This line
            implementation("dev.gitlive:firebase-common:$version")// This line
            implementation("dev.gitlive:firebase-functions:$version")// This line
        }
        iosMain.dependencies {
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    dependencies {
        implementation("com.google.firebase:firebase-common-ktx:21.0.0")
    }
}

android {
    namespace = "com.honeycomb.disciplineapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.honeycomb.disciplineapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    ksp(libs.room.compiler)
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}

swiftklib {
    create("appblocker") {
        path = file("../iosApp/iosApp/appblocker")
        packageName("com.honeycomb.appblocker")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}