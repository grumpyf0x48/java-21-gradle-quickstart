plugins {
    application
    java
    id("org.graalvm.buildtools.native")
}

val javaVersion: String by project

val junitVersion: String by project

//
// Maintenant, pour compiler en natif
// JUnit exige de moi que j'initialise au build 17 classes (dont 16 internes à son implémentation)
// 👎
val initializeAtBuildTime = listOf(
    "org.junit.jupiter.api.DisplayNameGenerator\$IndicativeSentences",
    "org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor\$ClassInfo",
    "org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor\$LifecycleMethods",
    "org.junit.jupiter.engine.descriptor.ClassTemplateInvocationTestDescriptor",
    "org.junit.jupiter.engine.descriptor.ClassTemplateTestDescriptor",
    "org.junit.jupiter.engine.descriptor.DynamicDescendantFilter\$Mode",
    "org.junit.jupiter.engine.descriptor.ExclusiveResourceCollector\$1",
    "org.junit.jupiter.engine.descriptor.MethodBasedTestDescriptor\$MethodInfo",
    "org.junit.jupiter.engine.discovery.ClassSelectorResolver\$DummyClassTemplateInvocationContext",
    "org.junit.platform.engine.support.store.NamespacedHierarchicalStore\$EvaluatedValue",
    "org.junit.platform.launcher.core.DiscoveryIssueNotifier",
    "org.junit.platform.launcher.core.HierarchicalOutputDirectoryProvider",
    "org.junit.platform.launcher.core.LauncherDiscoveryResult\$EngineResultInfo",
    "org.junit.platform.launcher.core.LauncherPhase",
    "org.junit.platform.suite.engine.DiscoverySelectorResolver",
    "org.junit.platform.suite.engine.SuiteTestDescriptor\$DiscoveryIssueForwardingListener",
    "org.junit.platform.suite.engine.SuiteTestDescriptor\$LifecycleMethods"
)

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
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
            buildArgs.add("--initialize-at-build-time=${initializeAtBuildTime.joinToString(",")}")
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
