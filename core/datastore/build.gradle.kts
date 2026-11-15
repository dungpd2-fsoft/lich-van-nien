plugins { id("lich.android.library") }
android { namespace = "com.lichvannien.core.datastore" }

dependencies { api(project(":core:model")); implementation(libs.datastore); implementation(libs.coroutines) }
