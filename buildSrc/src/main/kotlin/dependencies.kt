const val versionKotlin = "1.3.50"

object ClassPath {

    object Version {
        const val androidGradle = "3.5.0"
        const val dexcount = "0.8.6"
        const val fabric = "1.29.0"
        const val googleService = "4.3.4"
    }

    const val androidGradle = "com.android.tools.build:gradle:${Version.androidGradle}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:$versionKotlin"
    const val dexcount = "com.getkeepsafe.dexcount:dexcount-gradle-plugin:${Version.dexcount}"
    const val fabric = "io.fabric.tools:gradle:${Version.fabric}"
    const val googleService = "com.google.gms:google-services:${Version.googleService}"
}

object Maven {

    const val jitpack = "https://jitpack.io"
    const val fabric = "https://maven.fabric.io/public"
}

object AndroidPlugin {

    const val application = "com.android.application"
    const val library = "com.android.library"
    const val kotlin = "kotlin-android"
    const val kotlinExtensions = "kotlin-android-extensions"
    const val kotlinKapt = "kotlin-kapt"
    const val report = "project-report"
    const val dexcount = "com.getkeepsafe.dexcount"
    const val fabric = "io.fabric"
    const val googleService = "com.google.gms.google-services"
}

object Android {
    const val buildTools = "29.0.2"
    const val compile = 29
    const val min = 23
    const val target = 29
    const val cMake = "3.10.2"
}

object LibTest {

    private object Version {
        const val junit = "4.12"
        const val testRunner = "1.1.0"
        const val espresso = "3.2.0"
    }

    const val junit = "junit:junit:${Version.junit}"
    const val testRunner = "androidx.test:runner:${Version.testRunner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Version.espresso}"
}

object LibKotlin {

    const val jdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$versionKotlin"
}

object LibAndroidX {

    private object Version {
        const val appCompat = "1.0.2"
        const val legacyUtils = "1.0.0"
        const val exifInterface = "1.0.0"
        const val constraintLayout = "1.1.3"
        const val material = "1.0.0"

        const val navigationFragment = "2.2.2"
        const val navigationUi = "2.2.2"
        const val navigationFragmentKtx = "2.2.2"
        const val navigationUiKtx = "2.2.2"
    }

    const val appCompat = "androidx.appcompat:appcompat:${Version.appCompat}"
    const val legacyUtils = "androidx.legacy:legacy-support-core-utils:${Version.legacyUtils}"
    const val exifInterface = "androidx.exifinterface:exifinterface:${Version.exifInterface}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    const val material = "com.google.android.material:material:${Version.material}"

    const val navigationFragment = "androidx.navigation:navigation-fragment:${Version.navigationFragment}"
    const val navigationUi = "androidx.navigation:navigation-ui:${Version.navigationUi}"
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Version.navigationFragmentKtx}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Version.navigationUiKtx}"
}

object LibAndroidXLifecycle {

    private object Version {
        const val extension = "2.0.0"
        const val viewModel = "2.0.0"
        const val liveData = "2.0.0"
    }

    const val extension = "androidx.lifecycle:lifecycle-extensions:${Version.extension}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel:${Version.viewModel}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata:${Version.liveData}"
}

object LibModule {

    const val simpleAccountManager = ":simpleaccountmanager"
    const val easierSpinner = ":easierspinner"
    const val imagePicker = ":imagepicker"
    const val permissionHelper = ":permissionhelper"
}

object Lib {

    private object Version {
        const val retrofit = "2.6.1"
        const val retrofiConverterGson = "2.6.1"
        const val retrofitLogger = "3.12.3"
        const val gson = "2.8.5"
        const val gander = "3.1.0"
        const val crashlytics = "2.10.1"
        const val customCrashActivity = "2.2.0"
        const val imageCropper = "2.7.0"
        const val kensBurn = "1.0.7"
        const val firebaseAnalytics = "17.2.1"
        const val firebaseAuth = "19.2.0"
        const val coroutinesCore = "1.3.2"
        const val coroutinesAndroid = "1.3.2"
        const val otpView = "v1.1.2-ktx"
        const val barcode = "1.9.8"
        const val hawk = "2.0.1"
        const val circleImage = "3.1.0"
        const val MaskEditText = "3.1.1"
    }

    const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    const val retrofiConverterGson = "com.squareup.retrofit2:converter-gson:${Version.retrofiConverterGson}"
    const val retrofitLogger = "com.squareup.okhttp3:logging-interceptor:${Version.retrofitLogger}"
    const val gson = "com.google.code.gson:gson:${Version.gson}"
    const val gander = "com.ashokvarma.android:gander-persistence:${Version.gander}"
    const val ganderNoOp = "com.ashokvarma.android:gander-no-op:${Version.gander}"
    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Version.crashlytics}"
    const val customCrashActivity = "cat.ereza:customactivityoncrash:${Version.customCrashActivity}"
    const val imageCropper = "com.theartofdev.edmodo:android-image-cropper:${Version.imageCropper}"
    const val kensBurnAnimation = "com.flaviofaria:kenburnsview:${Version.kensBurn}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutinesCore}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutinesAndroid}"

    const val firebaseAnalytic = "com.google.firebase:firebase-analytics:${Version.firebaseAnalytics}"
    const val firebaseAuth = "com.google.firebase:firebase-auth:${Version.firebaseAuth}"
    const val otpView = "com.github.aabhasr1:OtpView:${Version.otpView}"
    const val barcode = "me.dm7.barcodescanner:zxing:${Version.barcode}"
    const val hawk = "com.orhanobut:hawk:${Version.hawk}"
    const val circleImage = "de.hdodenhof:circleimageview:${Version.circleImage}"
    const val MaskEditText = "com.vicmikhailau:MaskedEditText:${Version.MaskEditText}"
}

object ProGuard {

    const val defaultAndroid = "proguard-android.txt"

    const val app = "proguard/app.pro"
    const val android = "proguard/libs/android.pro"
    const val androidX = "proguard/libs/androidx.pro"
    const val crashlytics = "proguard/libs/crashlytics2.pro"
    const val fabric = "proguard/libs/fabric.pro"
    const val google = "proguard/libs/google.pro"
    const val gson = "proguard/libs/gson.pro"
    const val itsmagic = "proguard/libs/itsmagic.pro"
    const val okhttp = "proguard/libs/okhttp3.pro"
    const val retrofit = "proguard/libs/retrofit2.pro"
}
