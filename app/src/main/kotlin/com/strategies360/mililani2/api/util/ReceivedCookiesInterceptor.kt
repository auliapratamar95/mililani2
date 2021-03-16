package com.strategies360.mililani2.api.util

import android.content.Context
import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.io.IOException

class ReceivedCookiesInterceptor(context: Context) : Interceptor {
  private val context: Context = context

  @Throws(IOException::class) override fun intercept(chain: Chain): Response {
    val originalResponse: Response = chain.proceed(chain.request())
    if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
      val cookies = PreferenceManager.getDefaultSharedPreferences(context)
          .getStringSet("PREF_COOKIES", HashSet()) as HashSet<String>?
      for (header in originalResponse.headers("Set-Cookie")) {
        cookies!!.add(header)
      }
      val memes = PreferenceManager.getDefaultSharedPreferences(context).edit()
      memes.putStringSet("PREF_COOKIES", cookies).apply()
      memes.commit()
    }
    return originalResponse
  }
}