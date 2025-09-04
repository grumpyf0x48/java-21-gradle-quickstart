plugins {
    application
    java
    id("org.graalvm.buildtools.native")
}

val javaVersion: String by project

val junitVersion: String by project
val junitPlatformLauncherVersion: String by project

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${junitPlatformLauncherVersion}")
}

application {
    mainClass.set("org.grumpyf0x48.gradle_quickstart.Application")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

distributions {
    create("native") {
        contents {
            from(layout.buildDirectory.dir("native/nativeCompile").get()) {
                include(rootProject.name)
                into("bin")
            }
        }
    }
}

tasks.getByName("nativeDistZip") {
    dependsOn(tasks.nativeCompile)
    description = "Bundles the project as a native distribution."
}

tasks.getByName("installNativeDist") {
    dependsOn(tasks.nativeCompile)
    description = "Installs the project as a native distribution as-is."
}

tasks.getByName("distTar") {
    group = null
}

tasks.getByName("nativeDistTar") {
    group = null
}

graalvmNative {
    toolchainDetection.set(false)
    binaries {
        all {
            resources.autodetect()
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}
