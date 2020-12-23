package com.strategies360.mililani2.api

import com.strategies360.mililani2.util.JNIUtil
import com.strategies360.mililani2.api.util.OkHttpClientHelper
import com.strategies360.mililani2.model.remote.auth.ProfileResponse
import com.strategies360.mililani2.model.remote.auth.SignInRequest
import com.strategies360.mililani2.model.remote.auth.SignInResponse
import com.strategies360.mililani2.model.remote.product.SampleProductListResponse

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *
 * The API interface service.
 */
object APIService {

    /** The [APIInterface] object */
    val apiInterface: APIInterface by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(JNIUtil.apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClientHelper().initOkHttpClient())
                .build()

        retrofit.create(APIInterface::class.java)
    }

    /** The interface for retrofit's API calls */
    interface APIInterface {

        /** Logs the user into the system */
        @POST("login")
        fun signIn(
                // For easy visibility (test purposes), this sample will use a query
                @Query("Api-Key") apiKey: String?,
                //                @Header("Api-Key") String apiKey,
                @Body body: SignInRequest?)
                : Call<SignInResponse>

        /** Obtain the user profile */
        @GET("profile")
        fun getProfile(
                // For easy visibility (test purposes), this sample will use a query
                @Query("Api-Key") apiKey: String?,
                @Query("Token") accessToken: String?)
//                @Header("Api-Key") String apiKey,
//                @Header("Token") String accessToken);
                : Call<ProfileResponse>

        /** Obtain the product list */
        @GET("products")
        fun getProducts(
                @Header("Api-Key") apiKey: String?,
                @Header("Token") accessToken: String?,
                @Query("page") page: Int?)
                : Call<SampleProductListResponse>
    }
}
