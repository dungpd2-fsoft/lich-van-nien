plugins { alias(libs.plugins.kotlin.serialization); id("lich.android.library") }
android { namespace = "com.lichvannien.feature.settings.api" }

dependencies { api(project(":core:navigation")) }
