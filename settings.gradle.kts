rootProject.name = 'wave-mapper'

include("domain")
include("infrastructure")
include("wave-app")
include("sail-engine")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

