plugins {
    id("idea")
    `java-test-fixtures`
    `kotlin-dsl`
    alias(libs.plugins.kotlinserialization)
}

dependencies {
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.googleCloud)
    implementation(libs.bundles.jackson)
    implementation(libs.bundles.http4k)
    implementation(project(":domain"))

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.bundles.testRuntime)
    testFixturesApi(libs.http4kTestingChaos)
    testFixturesImplementation(libs.junitJupiterApi)

//    testImplementation project(path: ":domain", configuration: "testArtifact")

//    testArtifact sourceSets.test.output

}