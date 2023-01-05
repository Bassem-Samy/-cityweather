pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven(url = "https://jitpack.io")
        mavenCentral()
    }

    versionCatalogs {
        create("libraries") {
            from(files("gradle/libraries.toml"))
        }
        create("testLibraries") {
            from(files("gradle/test_libraries.toml"))
        }
    }
}
rootProject.name = "CityWeather"
include(":app")
