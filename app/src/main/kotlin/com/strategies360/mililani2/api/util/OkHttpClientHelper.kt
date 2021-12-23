package com.strategies360.mililani2.api.util

import com.ashokvarma.gander.GanderInterceptor
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.App
import com.strategies360.mililani2.BuildConfig
import com.strategies360.mililani2.util.Constant
import okhttp3.*
import okhttp3.Interceptor.Chain
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 */
class OkHttpClientHelper {

    /**
     * Initialize the [OkHttpClient] for retrofit.
     *
     * Server uses SSL, we need to install the certificate on the [OkHttpClient].
     * Debug and mock builds doesn't have valid certificates, so we'll allow all connections there.
     *
     * To whitelist specific domains to enable non-HTTPS connections,
     * check the **res/xml/network_security_config.xml** file.
     * @see <a href="https://developer.android.com/training/articles/security-config">link</a>
     */
    fun initOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        // TODO: Configure SSL pinning as needed
//        val certificatePinner = if (BuildConfig.FLAVOR.equals(JNIUtil.API_PRODUCTION, ignoreCase = true)) {
//            CertificatePinner.Builder()
//                    .add("api.example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
//                    .add("*.example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
//                    .build()
//        } else {
//            CertificatePinner.Builder()
//                    .add("test.example.com", "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=")
//                    .add("*.example.com", "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=")
//                    .build()
//        }
//        okHttpClientBuilder.certificatePinner(certificatePinner)

        // Add logging for debug builds
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
//            okHttpClientBuilder.addInterceptor(AddCookiesInterceptor(App.context)); // VERY VERY IMPORTANT
//            okHttpClientBuilder.addInterceptor(CookieInterceptor());
            okHttpClientBuilder.addInterceptor(logging)
        }

        // Add Gander interceptor
        okHttpClientBuilder.addInterceptor(
                GanderInterceptor(App.context)
                        .showNotification(true)
        )

        // Set timeout duration
        okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)

        return okHttpClientBuilder.build()
    }

    fun initCustomOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        // Add logging for debug builds
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logging)
        }

        // Add Gander interceptor
        okHttpClientBuilder.addInterceptor(
                GanderInterceptor(App.context)
                        .showNotification(true)
        )
        okHttpClientBuilder.addInterceptor(
                BasicAuthInterceptor("s360", "weg")
        )

        // Set timeout duration
        okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)

        return okHttpClientBuilder.build()
    }

    internal class CookieInterceptor : Interceptor {
        @Volatile
        private var cookie: String? = null
        @Throws(IOException::class) override fun intercept(chain: Chain): Response {
            var request: Request = chain.request()
            if (Hawk.contains(Constant.KEY_CUSTOMER_ID)) {
//                val expiration = Date(System.currentTimeMillis() + 60 * 60 * 1000)
//                val expires: String = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
//                    .format(expiration)
//                cookie =
//                    "customerId=" + Hawk.get(Constant.KEY_CUSTOMER_ID + "; " +
//                        "path=/; " +
//                        "expires=" + expires)
            }
            if (cookie != null) {
                request = request.newBuilder()
                    .header("set-cookie", cookie!!)
                    .build()
            }
            return chain.proceed(request)
        }
    }

    class BasicAuthInterceptor(user: String?, password: String?) : Interceptor {
        private val credentials: String = Credentials.basic(user.toString(), password.toString())

        @Throws(IOException::class)
        override fun intercept(chain: Chain): Response {
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build()
            return chain.proceed(authenticatedRequest)
        }

    }
}
