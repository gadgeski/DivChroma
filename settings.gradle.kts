pluginManagement {
    repositories {
        // HiltやKSPの依存関係を正しく解決するため、制限(contentブロック)を撤廃します
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "DivChroma"
include(":app")