plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.mobile.apps.proyectofinalappsmoviles"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mobile.apps.proyectofinalappsmoviles"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    // Dependencias de Jackson
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.3")
    //Dependencias de Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth:21.0.1")
    // Dependencias de Volley
    implementation("com.android.volley:volley:1.2.1")
    // Dependencias de Picasso
    implementation("com.squareup.picasso:picasso:2.71828")
    // Dependencia de ImagePicker
    implementation("io.github.ParkSangGwon:tedimagepicker:1.2.8")
    // Dependencia de OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}