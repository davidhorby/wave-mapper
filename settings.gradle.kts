rootProject.name = "wave-mapper"

include("shared","wave-app")

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

