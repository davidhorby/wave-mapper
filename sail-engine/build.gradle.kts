val invoker: Configuration by configurations.creating

//https://github.com/mwhyte-dev/kotlin-google-cloud-function/blob/main/build.gradle.kts
//
//plugins {
//    alias(libs.plugins.shadowJar)
//}

configurations {
    invoker
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.dhorby.gcloud.BucketToDSFunc"
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":shared"))

    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.googleCloud)
    implementation(libs.bundles.jackson)

    invoker("com.google.cloud.functions.invoker:java-function-invoker:1.1.1")

    testImplementation(testFixtures(project(":shared")))
//    testImplementation(project(mapOf(":domain" to "testArtifact")))

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.bundles.testRuntime)

}

task<JavaExec>("runFunction") {
    mainClass = "com.google.cloud.functions.invoker.runner.Invoker"
    classpath(invoker)
    inputs.files(configurations.runtimeClasspath, sourceSets["main"].output)
    args(
        "--target", project.findProperty("runFunction.target") ?: "",
        "--port", project.findProperty("runFunction.port") ?: 8080
    )
    doFirst {
        args("--classpath", files(configurations.runtimeClasspath, sourceSets["main"].output).asPath)
    }
}





