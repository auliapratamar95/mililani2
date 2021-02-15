plugins {
    id(AndroidPlugin.application)
    id(AndroidPlugin.kotlin)
    id(AndroidPlugin.kotlinExtensions)
    id(AndroidPlugin.kotlinKapt)
    id(AndroidPlugin.report)
    id(AndroidPlugin.dexcount)
    id(AndroidPlugin.fabric)
    id(AndroidPlugin.googleService)
}

// To view the dependency tree, open your terminal and run this command:
// gradlew htmlDependencyReport
//
// The result can be viewed in
// "\\build\reports\project\dependencies\index.html"

android {
    buildToolsVersion(Android.buildTools)
    compileSdkVersion(Android.compile)

    defaultConfig {
        applicationId = "com.strategies360.mililani2"
        minSdkVersion(Android.min)
        targetSdkVersion(Android.target)
        versionCode = 1
        versionName = "ver-1.0.5-debug"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Add a BuildConfig timestamp
        buildConfigField("long", "M_TIMESTAMP", "${System.currentTimeMillis()}L")

        // Manifest placeholders
        manifestPlaceholders = mapOf(
            "FABRIC_API_KEY" to "d5443dc729a470b899c6c7b732d3a3db59d37e2d")
    }

    // Enable Java 8 features
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    // Set the source path
    sourceSets["main"].java.srcDir("src/main/kotlin")
    sourceSets["test"].java.srcDir("src/test/kotlin")

//    // Split APKs for each screen dimension
//    splits {
//        // Configures multiple APKs based on screen density.
//        density {
//            enable true
//        }
//    }

    // Specify the build dimension and flavors
    flavorDimensions("api")
    productFlavors {
        create("mock") {
            setDimension("api")
            applicationIdSuffix = ".mock"
            versionNameSuffix = "-mock"

//            E.g. to use the manifest placeholder in your manifest, use ${FACEBOOK_APP_ID}
//            manifestPlaceholders["FACEBOOK_APP_ID"] = "12345"
//            manifestPlaceholders["FABRIC_API_KEY"] = "12345"
        }
        create("staging") {
            setDimension("api")
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
        }
        create("production") {
            setDimension("api")
        }

//        create("free") {
//            setDimension("mode")
//            applicationIdSuffix = ".free"
//        }
//        create("pro") {
//            setDimension("mode")
//        }
    }

    // Configure the build types
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(ProGuard.defaultAndroid),
                ProGuard.app,
                ProGuard.android,
                ProGuard.androidX,
                ProGuard.crashlytics,
                ProGuard.fabric,
                ProGuard.google,
                ProGuard.gson,
                ProGuard.itsmagic,
                ProGuard.okhttp,
                ProGuard.retrofit)
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile(ProGuard.defaultAndroid),
                ProGuard.app,
                ProGuard.android,
                ProGuard.androidX,
                ProGuard.crashlytics,
                ProGuard.fabric,
                ProGuard.google,
                ProGuard.gson,
                ProGuard.itsmagic,
                ProGuard.okhttp,
                ProGuard.retrofit)
        }
    }

    externalNativeBuild {
        cmake {
            setPath("src/main/cpp/CMakeLists.txt")
            setVersion(Android.cMake)
        }
    }
}

dependencies {
    implementation("androidx.viewpager:viewpager:1.0.0")
  testImplementation(LibTest.junit)
    androidTestImplementation(LibTest.testRunner)
    androidTestImplementation(LibTest.espresso)

    // .jar libs
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin
    implementation(LibKotlin.jdk)

    // AndroidX
    implementation(LibAndroidX.appCompat)
    implementation(LibAndroidX.legacyUtils)
    implementation(LibAndroidX.exifInterface)
    implementation(LibAndroidX.constraintLayout)
    implementation(LibAndroidX.material)

    implementation(LibAndroidX.navigationFragment)
    implementation(LibAndroidX.navigationFragmentKtx)
    implementation(LibAndroidX.navigationUi)
    implementation(LibAndroidX.navigationUiKtx)

    implementation(LibAndroidXLifecycle.extension)
    implementation(LibAndroidXLifecycle.viewModel)
    implementation(LibAndroidXLifecycle.liveData)

    // Modules
    implementation(project(LibModule.simpleAccountManager))
    implementation(project(LibModule.easierSpinner))
    implementation(project(LibModule.imagePicker))
    implementation(project(LibModule.permissionHelper))

    // Other libraries
    implementation(Lib.retrofit)
    implementation(Lib.retrofiConverterGson)
    implementation(Lib.retrofitLogger)
    implementation(Lib.gson)
    debugImplementation(Lib.gander)
    releaseImplementation(Lib.ganderNoOp)
    implementation(Lib.crashlytics)
    implementation(Lib.customCrashActivity)
    implementation(Lib.imageCropper)
    implementation(Lib.kensBurnAnimation)
    implementation(Lib.coroutinesCore)
    implementation(Lib.coroutinesAndroid)
    implementation(Lib.otpView)
    implementation(Lib.barcode)
    implementation(Lib.hawk)
    implementation(Lib.circleImage)
    implementation(Lib.MaskEditText)
    implementation(Lib.Coil)
    implementation(Lib.eventBus)
    implementation(Lib.scrollPagerIndicator)
    implementation(Lib.customMasked)

    implementation(Lib.firebaseAnalytic)
    implementation(Lib.firebaseAuth)

}

dexcount {
    includeTotalMethodCount = true
}
