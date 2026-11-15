plugins { id("com.android.library") }
android {
    compileSdk { version = release(37) { minorApiLevel = 0 } }
    defaultConfig { minSdk = 26 }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    flavorDimensions += "environment"
    productFlavors { create("demo") { dimension = "environment" }; create("prod") { dimension = "environment" } }
}
