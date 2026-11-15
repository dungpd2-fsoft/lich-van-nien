pluginManagement {
    includeBuild("build-logic")
    repositories { google { content { includeGroupByRegex("com\\.android.*"); includeGroupByRegex("com\\.google.*"); includeGroupByRegex("androidx.*") } }; mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google { content { includeGroupByRegex("com\\.android.*"); includeGroupByRegex("com\\.google.*"); includeGroupByRegex("androidx.*") } }; mavenCentral() }
}
rootProject.name = "LichVanNien"
include(":app")

include(":core:model")

include(":core:domain")

include(":core:designsystem")

include(":core:ui")

include(":core:navigation")

include(":feature:calendar:api")

include(":feature:calendar:impl")

include(":feature:settings:api")

include(":feature:settings:impl")

include(":feature:about:api")

include(":feature:about:impl")

include(":core:network")

include(":core:database")

include(":core:datastore")

include(":core:data")
