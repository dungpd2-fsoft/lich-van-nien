plugins { alias(libs.plugins.ksp); id("lich.android.library") }
android { namespace = "com.lichvannien.core.database" }

dependencies { api(project(":core:model")); implementation(libs.coroutines); api(libs.room.runtime); implementation(libs.room.ktx); ksp(libs.room.compiler) }
ksp { arg("room.schemaLocation", "$projectDir/schemas") }
