//import com.google.cloud.tools.gradle.appengine.core.deployextension
//import com.google.cloud.tools.gradle.appengine.standard.runextension
import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension

buildscript {
    repositories {
        gradlePluginPortal()
        google()
    }
    dependencies {
        classpath ("com.google.cloud.tools:appengine-gradle-plugin:2.4.4")
    }
}

plugins {
    alias(libs.plugins.shadowJar)
//    id("com.github.johnrengelman.shadow") version "7.0.0"
//    id("com.github.johnrengelman.shadow")
    application
//    "com.google.cloud.tools.appengine"
//    id("com.google.cloud.tools.appengine") version "2.1.0"
}

apply(plugin = "com.google.cloud.tools.appengine")
apply(plugin = "com.github.johnrengelman.shadow")

//allprojects {
//    apply(plugin = "com.github.johnrengelman.shadow")
//    apply(plugin = "com.google.cloud.tools.appengine")
//    apply(plugin = "application")
////}

application {
    mainClass = "com.dhorby.wavemapper.WaveMapperHttp4kApp"
}


//shadowJar {
//    dependsOn(jar)
//    archiveBaseName.set(project.name)
//    archiveClassifier.set(null)
//    archiveVersion.set(null)
//    mergeServiceFiles()
//}

//distTar.dependsOn(shadowJar)
//startScripts.dependsOn(shadowJar)
//distZip.dependsOn(shadowJar)

dependencies {

    implementation(libs.bundles.kotlin)
    implementation(libs.result4k)
    implementation(libs.bundles.http4k)
    implementation(libs.bundles.jackson)
    implementation(libs.bundles.googleCloud)

    implementation(project(":domain"))
    implementation(project(":infrastructure"))

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.googleCloudAppEngineTest)
    testImplementation(testFixtures(project(":infrastructure")))
    testFixturesApi(libs.http4kTestingChaos)
    testRuntimeOnly(libs.bundles.testRuntime)
}



//tasks.register("appengineDeploy") {
//    dependsOn(tasks.named("shadowJar"))
//}

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


//// Always run unit tests
////test.dependsOn shadowJar
//appengineDeploy.dependsOn test
//appengineStage.dependsOn(test)
//
//appengine {  // App Engine tasks configuration
//    deploy {   // deploy configuration
//        projectId = 'analytics-springernature'
//        version = '1'
//    }
//}
