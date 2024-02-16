buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.google.cloud.tools:appengine-gradle-plugin:2.4.1")
    }
}


plugins {
    alias(libs.plugins.kotlinserialization)
}


dependencies {
    implementation(libs.bundles.kotlin)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.bundles.testRuntime)
}
