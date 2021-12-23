buildscript {
    val kotlin_version by extra("1.4.32")
    repositories {
        google()
        jcenter()
        mavenCentral()
        gradlePluginPortal()
        maven(Maven.jitpack)
        maven(Maven.fabric)
    }

    dependencies {
        classpath(ClassPath.androidGradle)
        classpath(ClassPath.kotlinGradle)
        classpath(ClassPath.dexcount)
        classpath(ClassPath.fabric)
        classpath(ClassPath.googleService)
        classpath("gradle.plugin.com.onesignal:onesignal-gradle-plugin:[0.12.10, 0.99.99]")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
//        classpath("gradle.plugin.com.onesignal:onesignal-gradle-plugin:[0.12.10, 0.99.99]")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(Maven.jitpack)
        maven(Maven.fabric)
    }
}

tasks.register("clean").configure {
    delete("build")
}
