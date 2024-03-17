import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.1"
}

group = "com.jankinwu"
version = "1.0.0"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    maven("https://maven.aliyun.com/repository/google")
    maven("https://maven.aliyun.com/repository/jcenter")
    maven("https://maven.aliyun.com/repository/gradle-plugin")
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
    implementation("com.github.kwhat:jnativehook:2.2.2")
    @OptIn(ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "Mouse Listener"
            packageVersion = "1.0.0"
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            windows {
                shortcut = true
                dirChooser = true
                menu = true
                upgradeUuid = "c3571647-90fb-4e2d-b89b-a8d4f67fd266"
                iconFile.set(project.file("mouse_1.ico"))
                packageName = "Mouse Listener"
            }
            linux {
                packageName = "Mouse_Listener_amd64"
                shortcut = true
            }
            macOS {
                packageName = "Mouse_Listener_macos_x64"
                dockName = "Mouse Listener"
            }
        }
    }
}