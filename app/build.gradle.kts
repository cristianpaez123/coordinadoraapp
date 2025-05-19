plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution") version "4.0.0"
}

android {
    namespace = "com.example.coordinadoraapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.coordinadoraapp"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    //dagger2
    implementation("com.google.dagger:dagger:2.44")
    annotationProcessor("com.google.dagger:dagger-compiler:2.44")
    // RxJava
    implementation ("io.reactivex.rxjava3:rxjava:3.1.6")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")
    // MLKit
    implementation ("com.google.mlkit:barcode-scanning:17.2.0")
    implementation ("androidx.camera:camera-core:1.3.1")
    implementation ("androidx.camera:camera-camera2:1.3.1")
    implementation ("androidx.camera:camera-lifecycle:1.3.1")
    implementation ("androidx.camera:camera-view:1.3.1")
    // volley
    implementation ("com.android.volley:volley:1.2.1")
    // gson
    implementation("com.google.code.gson:gson:2.10.1")
    //ApiGoogleMaps
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    //unit test
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.2.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("io.reactivex.rxjava3:rxjava:3.1.5")
    testImplementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    testImplementation("org.robolectric:robolectric:4.10.3")
}

tasks.withType<Test> {
    jvmArgs("-Dnet.bytebuddy.experimental=true")
}

firebaseAppDistribution {
    artifactType = "APK"
    releaseNotes = "Build automático desde GitHub Actions"
    serviceCredentialsFile = project.findProperty("firebaseServiceCredentials") as String? ?: ""
    testers = project.findProperty("firebaseTesters") as String? ?: ""
}