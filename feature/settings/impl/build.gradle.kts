plugins { id("lich.android.compose") }
android { namespace = "com.lichvannien.feature.settings.impl" }

dependencies { implementation(project(":feature:settings:api")); implementation(project(":core:ui")); implementation(project(":core:domain")); implementation(libs.lifecycle.viewmodel.compose); implementation(libs.lifecycle.compose) }
