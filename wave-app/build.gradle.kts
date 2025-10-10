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

    implementation(project(":domain"))
    implementation(project(":shared"))

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.googleCloudAppEngineTest)
    testImplementation(testFixtures(project(":shared")))
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
