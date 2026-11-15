plugins { alias(libs.plugins.hilt); alias(libs.plugins.ksp); id("lich.android.compose") }
android { namespace = "com.lichvannien.feature.calendar.impl" }

dependencies { implementation(project(":feature:calendar:api")); implementation(project(":core:ui")); implementation(project(":core:domain")); implementation(libs.lifecycle.viewmodel.compose); implementation(libs.lifecycle.compose) }

dependencies { implementation(libs.hilt.android); ksp(libs.hilt.compiler); implementation(libs.lifecycle.viewmodel.savedstate) }
