plugins { alias(libs.plugins.kotlin.serialization); id("lich.android.library") }
android { namespace = "com.lichvannien.feature.calendar.api" }

dependencies { api(project(":core:navigation")) }
