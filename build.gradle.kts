plugins {
    application
    java
    id("org.graalvm.buildtools.native")
}

val version: String by project
val javaVersion: String by project

val junitVersion: String by project

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
}

application {
    mainClass.set("org.grumpyf0x48.gradle_quickstart.Application")
}

java {
    sourceCompatibility = JavaVersion.toVersion("${javaVersion}")
    targetCompatibility = JavaVersion.toVersion("${javaVersion}")
}

distributions {
    create("native") {
        contents {
            from("${project.buildDir}/native/nativeCompile") {
                include("${rootProject.name}")
                into("bin")
            }
        }
    }
}

tasks.getByName("nativeDistZip") {
    dependsOn(tasks.nativeCompile)
}

tasks.getByName("nativeDistTar") {
    dependsOn(tasks.nativeCompile)
}

tasks.getByName("installNativeDist") {
    dependsOn(tasks.nativeCompile)
}

graalvmNative {
    toolchainDetection.set(false)
    binaries {
        all {
            buildArgs.add("--initialize-at-build-time=org.junit.platform.launcher.core.LauncherConfig")
            buildArgs.add("--initialize-at-build-time=org.junit.jupiter.engine.config.InstantiatingConfigurationParameterConverter")
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
