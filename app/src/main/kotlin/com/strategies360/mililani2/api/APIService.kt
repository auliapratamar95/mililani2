package com.strategies360.mililani2.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.api.util.OkHttpClientHelper
import com.strategies360.mililani2.model.remote.DateResponse
import com.strategies360.mililani2.model.remote.auth.ProfileResponse
import com.strategies360.mililani2.model.remote.auth.SignInMililaniRequest
import com.strategies360.mililani2.model.remote.auth.SignInMililaniResponse
import com.strategies360.mililani2.model.remote.auth.SignInRequest
import com.strategies360.mililani2.model.remote.auth.SignInResponse
import com.strategies360.mililani2.model.remote.caffe.CategoryDetailsProductResponse
import com.strategies360.mililani2.model.remote.caffe.CategoryListResponse
import com.strategies360.mililani2.model.remote.caffe.PayloadResponse
import com.strategies360.mililani2.model.remote.caffe.ProductCaffeResponse
import com.strategies360.mililani2.model.remote.caffe.cart.CartRequest
import com.strategies360.mililani2.model.remote.caffe.cart.CartResponse
import com.strategies360.mililani2.model.remote.caffe.checkout.BillingRequest
import com.strategies360.mililani2.model.remote.caffe.checkout.MonetraResp
import com.strategies360.mililani2.model.remote.caffe.checkout.PayTicketResponse
import com.strategies360.mililani2.model.remote.caffe.checkout.PickupRequest
import com.strategies360.mililani2.model.remote.caffe.checkout.PickupResponse
import com.strategies360.mililani2.model.remote.caffe.store.StoreResponse
import com.strategies360.mililani2.model.remote.mtaCard.ClassesListResponse
import com.strategies360.mililani2.model.remote.mtaCard.DeleteMtaCardRequest
import com.strategies360.mililani2.model.remote.mtaCard.MTACardListResponse
import com.strategies360.mililani2.model.remote.mtaCard.MTACardRequest
import com.strategies360.mililani2.model.remote.news.NewsResponse
import com.strategies360.mililani2.model.remote.product.SampleProductListResponse
import com.strategies360.mililani2.model.remote.tickets.EventsResponse
import com.strategies360.mililani2.util.Constant
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 *
 * The API interface service.
 */
object APIService {

  var gson: Gson = GsonBuilder()
      .setLenient()
      .create()

  /** The [APIInterface] object */
  val apiInterfaceMililani: APIInterface by lazy {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://api.mililanitown.s360.is/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClientHelper().initOkHttpClient())
        .build()

    retrofit.create(APIInterface::class.java)
  }

  /** The [APIInterface] object */
  val apiInterfaceApiaryMililani: APIInterface by lazy {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://private-9e2182-mililani.apiary-mock.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClientHelper().initOkHttpClient())
        .build()

    retrofit.create(APIInterface::class.java)
  }

  /** The [APIInterface] object */
  val apiInterfaceCustomMililani: APIInterface by lazy {
    var url = ""
    if (Hawk.contains(Constant.KEY_URL_PAYMENT)) {
      val payTicketResponse: PayTicketResponse = Hawk.get(Constant.KEY_URL_PAYMENT)
      url = payTicketResponse.payTicket?.ticketApiUrl.toString()
    }
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .client(OkHttpClientHelper().initOkHttpClient())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    retrofit.create(APIInterface::class.java)
  }

//  /** The [APIInterface] object */
//  val apiInterfaceCustomMililani: APIInterface by lazy {
//    var url = ""
//    if (Hawk.contains(Constant.KEY_URL_PAYMENT)) {
//      val payTicketResponse: PayTicketResponse = Hawk.get(Constant.KEY_URL_PAYMENT)
//      url = payTicketResponse.payTicket?.ticketApiUrl.toString()
//    }
//    val retrofit = Retrofit.Builder()
//        .baseUrl(url)
//        .addConverterFactory(SimpleXmlConverterFactory.create())
//        .client(OkHttpClientHelper().initOkHttpClient())
//        .build()
//
//    retrofit.create(APIInterface::class.java)
//  }

  /** The [APIInterface] object */
  val apiInterfaceCaffeMililani: APIInterface by lazy {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://rec7cafe.myncrsilver.com/")
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
      @Body body: SignInRequest?
    )
        : Call<SignInResponse>

    /** Logs the user into the system */
    @POST("users/login")
    fun signInMililani(
      @Body body: SignInMililaniRequest?
    )
        : Call<SignInMililaniResponse>

    /** Logs the user into the system */
    @POST("cards")
    fun submitMTACard(
      @Header("Authorization") accessToken: String?,
      @Body body: MTACardRequest?
    )
        : Call<SignInMililaniResponse>

    /** Change Nickname the user into the system */
    @PUT("cards")
    fun editNicknameMTACard(
      @Header("Authorization") accessToken: String?,
      @Query("card_number") cardNumber: String?,
      @Body mtaCardRequest: MTACardRequest?
    )
        : Call<MTACardListResponse>

    /** Change Nickname the user into the system */
    @PATCH("cards/make-default")
    fun defaultMTACard(
      @Header("Authorization") accessToken: String?,
      @Query("card_number") cardNumber: String?
    )
        : Call<MTACardListResponse>

    /** Logs the user into the system */
    @HTTP(method = "DELETE", path = "cards", hasBody = true)
    fun deleteMTACard(
      @Header("Authorization") accessToken: String?,
      @Body deleteMtaCardRequest: DeleteMtaCardRequest?
    )
        : Call<MTACardListResponse>

    /** Obtain the MTA Card list */
    @GET("cards")
    fun getMTACard(
      @Header("Authorization") accessToken: String?
    )
        : Call<MTACardListResponse>

    /** Obtain the MTA Card list */
    @GET("news")
    fun getNews(
      @Header("Authorization") accessToken: String?
    )
        : Call<NewsResponse>

    /** Obtain the MTA Card list */
    @GET("classes")
    fun getClass(
      @Header("Authorization") accessToken: String?
    )
        : Call<ClassesListResponse>

    /** Obtain the MTA Card list */
    @GET("classes")
    fun getClassAll(
      @Header("Authorization") accessToken: String?
    )
        : Call<ClassesListResponse>

    /** Obtain the MTA Card list */
    @GET("catalog/categories/{categoryId}/products")
    fun getCaffeAll(
      @Path(value = "categoryId", encoded = false) categoryId: String?,
      @Query("p") page: Int?,
      @Query("s") size: Int?
    )
        : Call<PayloadResponse>

    /** Obtain the MTA Card list */
    @GET("cafes/catalog")
    fun getProductCaffe(@Header("Authorization") accessToken: String?)
        : Call<ProductCaffeResponse>

    /** Obtain the MTA Card list */
    @GET("catalog/categories")
    fun getCategory(
      @Query("includeSubCategories") includeSubCategories: Boolean?
    )
        : Call<CategoryListResponse>

    /** Obtain the MTA Card list */
    @GET("cafes/catalog/products/{productId}")
    fun getCategoryDetailsProduct(
      @Header("Authorization") accessToken: String?,
      @Path(value = "productId", encoded = false) categoryId: String?
    )
        : Call<CategoryDetailsProductResponse>

    /** Obtain the MTA Card list */
    @GET("classes")
    fun getFilterClass(
      @Header("Authorization") accessToken: String?,
      @Query("activity_type") activityType: String?,
      @Query("sub_type") subType: String?,
      @Query("begin_date") beginDate: String?,
      @Query("end_date") endDate: String?,
      @Query("location") location: String?
    )
        : Call<ClassesListResponse>

    /** Obtain the MTA Card list */
    @GET("tickets")
    fun getTickets(
      @Query("ticket_code") ticketCode: String?,
      @Query("begin_event_date") beginEventDate: String?,
      @Query("end_event_date") endEventDate: String?,
      @Query("key") key: String?
    )
        : Call<EventsResponse>

    @GET("classes")
    fun getCustomFilterClass(
      @QueryMap options: MutableMap<String, String>
    ): Call<ClassesListResponse>?

    /** Logs the user into the system */
    @POST("cart/items")
    fun submitDataCart(
      @Header("Cookie") cookie: String?,
      @Body body: CartRequest?
    )
        : Call<PayloadResponse>

    /** Obtain the MTA Card list */
    @GET("cart")
    fun getCart(
      @Header("Cookie") cookie: String?
    )
        : Call<CartResponse>

    /** Obtain the MTA Card list */
    @GET("stores")
    fun getStores(
      @Header("Cookie") cookie: String?
    )
        : Call<StoreResponse>

    /** Obtain the MTA Card list */
    @GET("fulfillment/types/1/slots")
    fun getDates(
      @Header("Cookie") cookie: String?,
      @Query("store") store: String?,
      @Query("date") date: String?
    )
        : Call<DateResponse>

    /** Obtain the MTA Card list */
    @DELETE("cart/items/{productId}")
    fun deleteCart(
      @Path(value = "productId", encoded = false) productId: String?,
      @Header("Cookie") cookie: String?
    )
        : Call<CartResponse>

    /** Obtain the MTA Card list */
    @PUT("cart/pickup")
    fun putPickupCart(
      @Header("Cookie") cookie: String?,
      @Body pickupRequest: PickupRequest?
    )
        : Call<PickupResponse>

    /** Obtain the MTA Card list */
    @GET("cart/secure-pay/ticket")
    fun getPayTicket(
      @Header("Cookie") cookie: String?
    )
        : Call<PayTicketResponse>

    @FormUrlEncoded
    @POST(".")
    fun submitDataSecurePay(
      @Field("username") username: String,
      @Field("action") action: String?,
      @Field("admin") admin: String,
      @Field("monetra_req_sequence") monetraReqSequence: String,
      @Field("monetra_req_timestamp") monetraReqTimestamp: String,
      @Field("monetra_req_hmacsha256") monetraReqHmacsha256: String,
      @Field("account") account: String,
      @Field("cardholdername") cardholdername: String,
      @Field("expdate") expdate: String,
      @Field("cv") cv: String,
      @Field("zip") zip: String
    ): Observable<MonetraResp>

    /** Obtain the MTA Card list */
    @POST("cart/billing")
    fun submitBiling(
      @Header("Cookie") cookie: String?,
      @Body billingRequest: BillingRequest?
    )
        : Call<PickupResponse>

    /** Obtain the MTA Card list */
    @POST("cart/checkout")
    fun submitCheckout(
      @Header("Cookie") cookie: String?
    )
        : Call<PickupResponse>

    /** Obtain the MTA Card list */
    @PUT("cart/tip")
    fun submitTip(
      @Header("Cookie") cookie: String?, @Body tip: Double?
    )
        : Call<PickupResponse>

    /** Obtain the user profile */
    @GET("profile")
    fun getProfile(
        // For easy visibility (test purposes), this sample will use a query
      @Query("Api-Key") apiKey: String?,
      @Query("Token") accessToken: String?
    )
        : Call<ProfileResponse>

    /** Obtain the product list */
    @GET("products")
    fun getProducts(
      @Header("Api-Key") apiKey: String?,
      @Header("Token") accessToken: String?,
      @Query("page") page: Int?
    )
        : Call<SampleProductListResponse>

  }
}
