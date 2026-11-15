plugins { alias(libs.plugins.kotlin.serialization); id("lich.android.library") }
android { namespace = "com.lichvannien.core.network" }

dependencies { api(project(":core:model")); implementation(libs.coroutines); implementation(libs.retrofit); implementation(libs.retrofit.serialization); implementation(libs.okhttp); implementation(libs.serialization.json) }
