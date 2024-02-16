import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `idea`
    `java-library`
    `java-test-fixtures`
    `kotlin-dsl`
    alias(libs.plugins.kotlinjvm)
}

allprojects {
    project.group = "com.dhorby"
}

subprojects {

    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("java-test-fixtures")
    }

    tasks.test {
        useJUnitPlatform()
        testLogging {
            events(TestLogEvent.STANDARD_ERROR, TestLogEvent.FAILED)//, TestLogEvent.SKIPPED, TestLogEvent.PASSED
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }

}
