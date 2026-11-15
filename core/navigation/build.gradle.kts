plugins { id("lich.android.library") }
android { namespace = "com.lichvannien.core.navigation" }

dependencies { api(libs.navigation.runtime); api(libs.serialization.json) }
