buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath ("com.google.cloud.tools:appengine-gradle-plugin:2.4.1")
    }
}

plugins {
//    alias(libs.plugins.shadowJar)
//    id("com.github.johnrengelman.shadow") version "7.0.0"
//    id("com.github.johnrengelman.shadow")
    application
}

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

//    implementation(project(":domain"))
//    implementation(project("path" to ":infrastructure"))
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
//    mapOf("path" to ":http4k-testing-webdriver")

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.googleCloudAppEngineTest)
    testImplementation(testFixtures(project(":infrastructure")))
    testFixturesApi(libs.http4kTestingChaos)

//    testImplementation project( path: ":domain")

    testRuntimeOnly(libs.bundles.testRuntime)
}

// Always run unit tests
//test.dependsOn shadowJar
//appengineDeploy.dependsOn test
//appengineStage.dependsOn test

//appengine {  // App Engine tasks configuration
//    deploy {   // deploy configuration
//        projectId = 'analytics-springernature'
//        version = '1'
//    }
//}
