rootProject.name = "wave-mapper"

include("domain")
include("shared")
include("wave-app")
include("sail-engine")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

