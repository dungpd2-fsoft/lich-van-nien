plugins { alias(libs.plugins.android.application); alias(libs.plugins.kotlin.compose) }
android {
    namespace = "com.lichvannien"
    compileSdk { version = release(37) { minorApiLevel = 0 } }
    defaultConfig { applicationId = "com.lichvannien"; minSdk = 26; targetSdk = 37; versionCode = 1; versionName = "0.1.0" }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    buildFeatures { compose = true; buildConfig = true }
    flavorDimensions += "environment"
    productFlavors { create("demo") { dimension = "environment"; applicationIdSuffix = ".demo" }; create("prod") { dimension = "environment" } }
}
dependencies { implementation(libs.activity.compose); implementation(libs.material3); implementation(libs.compose.ui) }
