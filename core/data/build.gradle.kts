plugins { alias(libs.plugins.ksp); alias(libs.plugins.hilt); id("lich.android.library") }
android { namespace = "com.lichvannien.core.data" }

dependencies {
 api(project(":core:domain")); implementation(project(":core:network")); implementation(project(":core:database")); implementation(project(":core:datastore"))
 implementation(libs.coroutines.android); implementation(libs.serialization.json); implementation(libs.hilt.android); ksp(libs.hilt.compiler)
}
