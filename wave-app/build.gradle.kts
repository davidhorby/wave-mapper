import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension

buildscript {
    repositories {
        gradlePluginPortal()
        google()
    }
    dependencies {
        classpath("com.google.cloud.tools:appengine-gradle-plugin:2.4.4")
    }
}

plugins {
    id("com.gradleup.shadow") version "9.2.2"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
    `java-test-fixtures`
    application
}

apply(plugin = "com.google.cloud.tools.appengine")

application {
    mainClass = "com.dhorby.wavemapper.WaveMapperHttp4kApp"
}

dependencies {

    implementation(libs.bundles.kotlin)
    implementation(libs.result4k)
    implementation(libs.bundles.http4k)
    implementation(libs.bundles.jackson)
    implementation(libs.bundles.googleCloud)

    testFixturesImplementation(libs.bundles.googleCloud)
    testFixturesImplementation(libs.junitJupiterApi)
    testFixturesImplementation("org.testcontainers:gcloud:1.21.3")
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.googleCloudAppEngineTest)
    testImplementation(libs.testContainers)
    // https://mvnrepository.com/artifact/org.testcontainers/gcloud
    testImplementation("org.testcontainers:gcloud:1.21.3")
    testFixturesApi(libs.http4kTestingChaos)
    testRuntimeOnly(libs.bundles.testRuntime)
}

tasks.withType<ShadowJar> {
    isZip64 = true
    mergeServiceFiles()
}

configure<AppEngineAppYamlExtension> {
    stage {
        setArtifact("build/libs/${project.name}-all.jar")
    }
    deploy {
        projectId = "analytics-springernature"
        version = "1"
        stopPreviousVersion = true // etc
    }
}
