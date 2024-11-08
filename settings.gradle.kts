rootProject.name = "wave-mapper"

include("domain","shared","wave-app","sail-engine")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

