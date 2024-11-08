rootProject.name = "wave-mapper"

include("domain","shared","wave-app","sail-engine")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        maven {
            url = uri("https://springernature.jfrog.io/artifactory/libs-release/")
            credentials {
                username = System.getenv("ARTIFACTORY_USERNAME")
                password = System.getenv("ARTIFACTORY_PASSWORD")
            }

            mavenContent { releasesOnly() }
        }
        mavenCentral {
            mavenContent {
                releasesOnly()
            }
        }
    }
}

