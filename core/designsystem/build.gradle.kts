plugins { id("lich.android.compose") }
android { namespace = "com.lichvannien.core.designsystem" }
dependencies {
    api(project(":core:model"))
    api(libs.material3)
    api(libs.compose.foundation)
    api(libs.compose.ui)
    api(libs.compose.preview)
    debugImplementation(libs.compose.tooling)
}