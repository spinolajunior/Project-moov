pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Mudei para PREFER_SETTINGS
    repositories {
        google()
        mavenCentral()
        flatDir { // ADICIONEI ESTA SEÇÃO PARA O HERE SDK
            dirs("app/libs")
        }
    }
}

rootProject.name = "Moov"
include(":app")