/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.8.2/userguide/building_java_projects.html
 */

val graalvmVersion: String by project

group = "org.glavo"
version = "0.1.0"

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("org.mikeneck.graalvm-native-image") version "1.2.0"
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
}

dependencies {
    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.graalvm.truffle:truffle-api:$graalvmVersion")
    implementation("org.graalvm.sdk:launcher-common:$graalvmVersion")

    annotationProcessor("org.graalvm.truffle:truffle-dsl-processor:$graalvmVersion")
}

application {
    // Define the main class for the application.
    mainClass.set("org.glavo.lua.launcher.LuaLauncher")
}

tasks.compileJava {
    modularity.inferModulePath.set(true)
    options.release.set(11)
}

tasks.compileTestJava {
    options.release.set(11)
}

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}


nativeImage {
    graalVmHome = System.getenv("JAVA_HOME")
    mainClass = "org.glavo.lua.launcher.LuaLauncher"
    executableName = "grlua"
    outputDirectory = file("$buildDir/executable")
    dependsOn(rootProject.tasks.jar)
    arguments(
            "--class-path",
            rootProject.tasks.jar.get().archiveFile.get().toString(),
            "--macro:truffle",
            "--no-fallback",
            "--initialize-at-build-time"
    )
}
