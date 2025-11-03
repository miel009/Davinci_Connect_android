plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.davinciconnect"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.davinciconnect"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Valor seguro por defecto (Cloud Functions en producción)
        buildConfigField(
            "String",
            "FUNCTIONS_BASE_URL",
            "\"https://us-central1-davinciconnect-4817d.cloudfunctions.net/\""
        )
    }

    // Necesario para que se genere BuildConfig con nuestros fields
    buildFeatures { buildConfig = true }

    // Lee la URL de ngrok desde variable de entorno o gradle.properties (opcional)
    val ngrokUrl: String? = (System.getenv("NGROK_URL")
        ?: (project.findProperty("NGROK_URL") as String?))
        ?.trimEnd('/') // por si viene con "/"

    buildTypes {
        // DEBUG → usa NGROK si está definido; si no, cae a Cloud Functions o a 10.0.2.2
        getByName("debug") {
            val base = when {
                // Ej: NGROK_URL=https://abc123.ngrok.io
                ngrokUrl != null -> "$ngrokUrl/davinciconnect-4817d/us-central1/"
                else -> "http://10.0.2.2:5001/davinciconnect-4817d/us-central1/"
            }
            buildConfigField("String", "FUNCTIONS_BASE_URL", "\"$base\"")
        }

        // RELEASE → Cloud Functions
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "FUNCTIONS_BASE_URL",
                "\"https://us-central1-davinciconnect-4817d.cloudfunctions.net/\""
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // UI
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.activity)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase (BOM)
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    // Retrofit + Gson + logging
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
}
