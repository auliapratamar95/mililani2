#include <jni.h>

// Mock
extern "C" JNIEXPORT jstring JNICALL
Java_com_appschef_baseproject_util_JNIUtil_getWebUrlMock(JNIEnv *env, jobject /* this */) {
    return env->NewStringUTF("http://www.google.com/");
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_appschef_baseproject_util_JNIUtil_getApiUrlMock(JNIEnv *env, jobject /* this */) {
    return env->NewStringUTF("https://api-mililani2-dev.app.s360.is/v1/");
}

//extern "C" JNIEXPORT jstring JNICALL
//Java_com_appschef_baseproject_util_JNIUtil_getApiKeyMock(JNIEnv *env, jobject /* this */) {
//    return env->NewStringUTF("mymockapikey");
//}


// Staging
extern "C" JNIEXPORT jstring JNICALL
Java_com_appschef_baseproject_util_JNIUtil_getWebUrlStaging(JNIEnv *env, jobject /* this */) {
    return env->NewStringUTF("http://www.google.com/");
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_appschef_baseproject_util_JNIUtil_getApiUrlStaging(JNIEnv *env, jobject /* this */) {
    return env->NewStringUTF("http://private-778487-alvinrusliappschef.apiary-mock.com/");
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_appschef_baseproject_util_JNIUtil_getApiKeyStaging(JNIEnv *env, jobject /* this */) {
    return env->NewStringUTF("mymockapikey");
}


// Production
extern "C" JNIEXPORT jstring JNICALL
Java_com_appschef_baseproject_util_JNIUtil_getWebUrlProduction(JNIEnv *env, jobject /* this */) {
    return env->NewStringUTF("http://www.google.com/");
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_appschef_baseproject_util_JNIUtil_getApiUrlProduction(JNIEnv *env, jobject /* this */) {
    return env->NewStringUTF("http://private-778487-alvinrusliappschef.apiary-mock.com/");
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_appschef_baseproject_util_JNIUtil_getApiKeyProduction(JNIEnv *env, jobject /* this */) {
    return env->NewStringUTF("myproductionapikey");
}