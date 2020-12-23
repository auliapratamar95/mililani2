buildscript {
    repositories {
        google()
        jcenter()
        maven(Maven.jitpack)
        maven(Maven.fabric)
    }

    dependencies {
        classpath(ClassPath.androidGradle)
        classpath(ClassPath.kotlinGradle)
        classpath(ClassPath.dexcount)
        classpath(ClassPath.fabric)
        classpath(ClassPath.googleService)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(Maven.jitpack)
        maven(Maven.fabric)
    }
}

tasks.register("clean").configure {
    delete("build")
}
