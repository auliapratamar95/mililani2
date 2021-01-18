package com.strategies360.mililani2.api

import com.strategies360.mililani2.api.util.OkHttpClientHelper
import com.strategies360.mililani2.model.remote.auth.ProfileResponse
import com.strategies360.mililani2.model.remote.auth.SignInMililaniRequest
import com.strategies360.mililani2.model.remote.auth.SignInMililaniResponse
import com.strategies360.mililani2.model.remote.auth.SignInRequest
import com.strategies360.mililani2.model.remote.auth.SignInResponse
import com.strategies360.mililani2.model.remote.mtaCard.DeleteMtaCardRequest
import com.strategies360.mililani2.model.remote.mtaCard.MTACardListResponse
import com.strategies360.mililani2.model.remote.mtaCard.MTACardRequest
import com.strategies360.mililani2.model.remote.news.NewsResponse
import com.strategies360.mililani2.model.remote.product.SampleProductListResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.PATCH
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
                .baseUrl("https://api-mililani2-dev.app.s360.is/v1/")
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


      /** Logs the user into the system */
      @POST("users/login")
      fun signInMililani(
        @Body body: SignInMililaniRequest?)
          : Call<SignInMililaniResponse>

      /** Logs the user into the system */
      @POST("cards")
      fun submitMTACard(
        @Header("Authorization") accessToken: String?,
        @Body body: MTACardRequest?)
          : Call<SignInMililaniResponse>

      /** Change Nickname the user into the system */
      @PATCH("cards")
      fun editNicknameMTACard(
        @Header("Authorization") accessToken: String?,
        @Query("card_number") cardNumber: String?,
        @Body mtaCardRequest: MTACardRequest?)
          : Call<MTACardListResponse>

      /** Change Nickname the user into the system */
      @PATCH("cards/make-default")
      fun defaultMTACard(
        @Header("Authorization") accessToken: String?,
        @Query("card_number") cardNumber: String?)
          : Call<MTACardListResponse>

      /** Logs the user into the system */
      @HTTP(method = "DELETE", path = "cards", hasBody = true)
      fun deleteMTACard(
        @Header("Authorization") accessToken: String?,
        @Body deleteMtaCardRequest: DeleteMtaCardRequest?)
          : Call<MTACardListResponse>

      /** Obtain the MTA Card list */
      @GET("cards")
      fun getMTACard(
        @Header("Authorization") accessToken: String?)
          : Call<MTACardListResponse>


      /** Obtain the MTA Card list */
      @GET("news")
      fun getNews(
        @Header("Authorization") accessToken: String?)
          : Call<NewsResponse>



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
