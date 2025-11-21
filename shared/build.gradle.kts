plugins {
    id("idea")
    `java-test-fixtures`
    alias(libs.plugins.kotlinserialization)
}

dependencies {
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.googleCloud)
    implementation(libs.bundles.jackson)
    implementation(libs.bundles.http4k)

    testFixturesImplementation(libs.bundles.googleCloud)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.bundles.testRuntime)
    testFixturesApi(libs.http4kTestingChaos)
    testFixturesImplementation(libs.junitJupiterApi)

}