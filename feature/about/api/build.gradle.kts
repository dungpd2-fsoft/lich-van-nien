plugins { alias(libs.plugins.kotlin.serialization); id("lich.android.library") }
android { namespace = "com.lichvannien.feature.about.api" }

dependencies { api(project(":core:navigation")) }
