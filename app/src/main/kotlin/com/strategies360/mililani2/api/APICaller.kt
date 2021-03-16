package com.strategies360.mililani2.api

import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.api.callback.AppCallback
import com.strategies360.mililani2.api.callback.ProductListCallback
import com.strategies360.mililani2.api.callback.SignInCallback
import com.strategies360.mililani2.api.callback.core.CoreCallback
import com.strategies360.mililani2.api.util.OnAPIListener
import com.strategies360.mililani2.model.core.AppResponse
import com.strategies360.mililani2.model.remote.DateResponse
import com.strategies360.mililani2.model.remote.auth.*
import com.strategies360.mililani2.model.remote.caffe.CategoryDetailsProductResponse
import com.strategies360.mililani2.model.remote.caffe.CategoryListResponse
import com.strategies360.mililani2.model.remote.caffe.PayloadResponse
import com.strategies360.mililani2.model.remote.caffe.ProductCaffeResponse
import com.strategies360.mililani2.model.remote.caffe.cart.CartRequest
import com.strategies360.mililani2.model.remote.caffe.cart.CartResponse
import com.strategies360.mililani2.model.remote.caffe.store.StoreResponse
import com.strategies360.mililani2.model.remote.mtaCard.ClassesListResponse
import com.strategies360.mililani2.model.remote.mtaCard.DeleteMtaCardRequest
import com.strategies360.mililani2.model.remote.mtaCard.MTACardListResponse
import com.strategies360.mililani2.model.remote.mtaCard.MTACardRequest
import com.strategies360.mililani2.model.remote.news.NewsResponse
import com.strategies360.mililani2.model.remote.product.SampleProductListResponse
import com.strategies360.mililani2.util.Constant
import retrofit2.Call
import retrofit2.Callback

/**
 *
 * A class that handles all API calls.
 */
class APICaller<RESPONSE : AppResponse> {

  /** The access token for API calls */
  private var accessToken: String? = null

  /** The api key for API calls */
  private var apiKey: String? = "mock"

  /**
   * Determines if this API call uses a specific access token,
   * instead of using the access token from saved profile.
   */
  private var isUsingCustomToken = false

  /** The listener object for API calls */
  internal var listener: OnAPIListener<RESPONSE>? = null

  /** The [Call] object */
  private var call: Call<*>? = null

  /** The [CoreCallback] */
  private var callback: Callback<*>? = null

  /**
   * Sets the access token.
   * When called, this will override the default token loaded from saved data.
   * @param accessToken the access token
   * @return this
   */
  fun withAccessToken(accessToken: String): APICaller<RESPONSE> {
    this.accessToken = accessToken
    isUsingCustomToken = true
    return this
  }

  /**
   * Sets the API key.
   * When called, this will override the default API key.
   * @param apiKey the API key
   * @return this
   */
  fun withApiKey(apiKey: String): APICaller<RESPONSE> {
    this.apiKey = apiKey
    return this
  }

  /**
   * Adds a listener for the helper class.
   * @param listener the [OnAPIListener] object
   * @return this
   */
  fun withListener(listener: OnAPIListener<RESPONSE>): APICaller<RESPONSE> {
    this.listener = listener
    return this
  }

  /**
   * Checks if the call has been executed / enqueued.
   * Be careful, a call that has been executed will return true even on failure.
   */
  val isExecuted: Boolean?
    get() = call?.isExecuted

  /** Cancels any ongoing call */
  fun cancel() {
    call?.cancel()
  }

  /** Clears the current callback and call */
  fun clear() {
    callback = null
    call = null
  }

  /** Refresh the current access token with the token from the profile */
  private fun refreshAccessToken() {
    if (Hawk.contains(Constant.KEY_TOKEN)) accessToken = "Bearer " + Hawk.get(Constant.KEY_TOKEN)
//        if (!isUsingCustomToken) accessToken = AccountHelper.getToken(App.context)
  }

  /**
   * Calls the sign in API.
   * Logs the user in to the system.
   * @param data the data to be posted
   */
  @Suppress("UNCHECKED_CAST")
  fun signIn(data: SignInRequest) {
    refreshAccessToken()

    callback = SignInCallback(listener as OnAPIListener<SignInResponse>)
    call = APIService.apiInterfaceMililani.signIn(apiKey, data)
    (call as Call<SignInResponse>).enqueue(callback as SignInCallback)
  }

  /**
   * Calls the sign in API.
   * Logs the user in to the system.
   * @param data the data to be posted
   */
  @Suppress("UNCHECKED_CAST")
  fun signInMililani(data: SignInMililaniRequest) {
    callback = AppCallback(listener as OnAPIListener<SignInMililaniResponse>)
    call = APIService.apiInterfaceMililani.signInMililani(data)
    (call as Call<SignInMililaniResponse>).enqueue(callback as AppCallback<SignInMililaniResponse>)
  }

  /**
   * Calls the sign in API.
   * Logs the user in to the system.
   * @param data the data to be posted
   */
  @Suppress("UNCHECKED_CAST")
  fun submitMTACard(data: MTACardRequest?) {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<SignInMililaniResponse>)
    call = APIService.apiInterfaceMililani.submitMTACard(accessToken, data)
    (call as Call<SignInMililaniResponse>).enqueue(callback as AppCallback<SignInMililaniResponse>)
  }

  /**
   * Calls the sign in API.
   * Logs the user in to the system.
   * @param data the data to be posted
   */
  @Suppress("UNCHECKED_CAST")
  fun editNicknameMTACard(
      cardNumber: String?,
      data: MTACardRequest?
  ) {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<MTACardListResponse>)
    call = APIService.apiInterfaceMililani.editNicknameMTACard(accessToken, cardNumber, data)
    (call as Call<MTACardListResponse>).enqueue(callback as AppCallback<MTACardListResponse>)
  }

  /**
   * Calls the delete card in API.
   * Logs the user in to the system.
   */
  @Suppress("UNCHECKED_CAST")
  fun deleteMTACard(dataList: DeleteMtaCardRequest?) {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<MTACardListResponse>)
    call = APIService.apiInterfaceMililani.deleteMTACard(accessToken, dataList)
    (call as Call<MTACardListResponse>).enqueue(callback as AppCallback<MTACardListResponse>)
  }

  /**
   * Calls the sign in API.
   * Logs the user in to the system.
   * @param data the data to be posted
   */
  @Suppress("UNCHECKED_CAST")
  fun defaultMTACard(cardNumber: String??) {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<MTACardListResponse>)
    call = APIService.apiInterfaceMililani.defaultMTACard(accessToken, cardNumber)
    (call as Call<MTACardListResponse>).enqueue(callback as AppCallback<MTACardListResponse>)
  }

  /**
   * Calls the MTA Card in API.
   * Logs the user in to the system.
   * @param data the data to be posted
   */
  @Suppress("UNCHECKED_CAST")
  fun getMTACard() {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<MTACardListResponse>)
    call = APIService.apiInterfaceMililani.getMTACard(accessToken)
    (call as Call<MTACardListResponse>).enqueue(callback as AppCallback<MTACardListResponse>)
  }

  @Suppress("UNCHECKED_CAST")
  fun getNews() {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<NewsResponse>)
    call = APIService.apiInterfaceMililani.getNews(accessToken)
    (call as Call<NewsResponse>).enqueue(callback as AppCallback<NewsResponse>)
  }

  @Suppress("UNCHECKED_CAST")
  fun getClass() {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<ClassesListResponse>)
    call = APIService.apiInterfaceMililani.getClass(accessToken)
    (call as Call<ClassesListResponse>).enqueue(callback as AppCallback<ClassesListResponse>)
  }

  @Suppress("UNCHECKED_CAST")
  fun getClassAll() {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<ClassesListResponse>)
    call = APIService.apiInterfaceMililani.getClassAll(accessToken)
    (call as Call<ClassesListResponse>).enqueue(callback as AppCallback<ClassesListResponse>)
  }

  @Suppress("UNCHECKED_CAST")
  fun getFilterClass(
      activityType: String,
      subType: String,
      beginDate: String,
      endDate: String,
      location: String
  ) {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<ClassesListResponse>)
    call = APIService.apiInterfaceMililani.getFilterClass(
        accessToken, activityType, subType, beginDate, endDate, location
    )
    (call as Call<ClassesListResponse>).enqueue(callback as AppCallback<ClassesListResponse>)
  }

  @Suppress("UNCHECKED_CAST")
  fun getCustomFilterClass(data: MutableMap<String, String>) {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<ClassesListResponse>)
    call = APIService.apiInterfaceMililani.getCustomFilterClass(data)
    (call as Call<ClassesListResponse>).enqueue(callback as AppCallback<ClassesListResponse>)
  }

  @Suppress("UNCHECKED_CAST")
  fun getCaffeAll(
      categoryId: String,
      page: Int,
      size: Int
  ) {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<PayloadResponse>)
    call = APIService.apiInterfaceCaffeMililani.getCaffeAll(categoryId, page, size)
    (call as Call<PayloadResponse>).enqueue(callback as AppCallback<PayloadResponse>)
  }

  @Suppress("UNCHECKED_CAST")
  fun getProductCaffe() {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<ProductCaffeResponse>)
    call = APIService.apiInterfaceMililani.getProductCaffe(accessToken)
    (call as Call<ProductCaffeResponse>).enqueue(callback as AppCallback<ProductCaffeResponse>)
  }

  @Suppress("UNCHECKED_CAST")
  fun getCategory() {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<CategoryListResponse>)
    call = APIService.apiInterfaceCaffeMililani.getCategory(true)
    (call as Call<CategoryListResponse>).enqueue(callback as AppCallback<CategoryListResponse>)
  }

  @Suppress("UNCHECKED_CAST")
  fun getCategoryDetailsProduct(productId: String) {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<CategoryDetailsProductResponse>)
    call = APIService.apiInterfaceMililani.getCategoryDetailsProduct(accessToken, productId)
    (call as Call<CategoryDetailsProductResponse>).enqueue(
        callback as AppCallback<CategoryDetailsProductResponse>
    )
  }

  /**
   * Calls the sign in API.
   * Obtain the user profile.
   */
  @Suppress("UNCHECKED_CAST")
  fun getProfile() {
    refreshAccessToken()

    callback = AppCallback(listener as OnAPIListener<ProfileResponse>)
    call = APIService.apiInterfaceMililani.getProfile(apiKey, accessToken)
    (call as Call<ProfileResponse>).enqueue(callback as AppCallback<ProfileResponse>)
  }

  /**
   * Calls the sign in API.
   * Obtain the user profile.
   */
  @Suppress("UNCHECKED_CAST")
  fun submitDataCart(
      cookie: String,
      cartRequest: CartRequest
  ) {
    callback = AppCallback(listener as OnAPIListener<PayloadResponse>)
    call = APIService.apiInterfaceCaffeMililani.submitDataCart(cookie, cartRequest)
    (call as Call<PayloadResponse>).enqueue(callback as AppCallback<PayloadResponse>)
  }

  /**
   * Calls the cart in API.
   */
  @Suppress("UNCHECKED_CAST")
  fun getCart(cookie: String) {
    callback = AppCallback(listener as OnAPIListener<CartResponse>)
    call = APIService.apiInterfaceCaffeMililani.getCart(cookie)
    (call as Call<CartResponse>).enqueue(callback as AppCallback<CartResponse>)
  }

  /**
   * Calls the cart in API.
   */
  @Suppress("UNCHECKED_CAST")
  fun getStore(cookie: String) {
    callback = AppCallback(listener as OnAPIListener<StoreResponse>)
    call = APIService.apiInterfaceCaffeMililani.getStores(cookie)
    (call as Call<StoreResponse>).enqueue(callback as AppCallback<StoreResponse>)
  }

  /**
   * Calls the cart in API.
   */
  @Suppress("UNCHECKED_CAST")
  fun getDate(
      cookie: String,
      store: String,
      date: String
  ) {
    callback = AppCallback(listener as OnAPIListener<DateResponse>)
    call = APIService.apiInterfaceCaffeMililani.getDates(cookie, store, date)
    (call as Call<DateResponse>).enqueue(callback as AppCallback<DateResponse>)
  }

  /**
   * Calls the cart in API.
   */
  @Suppress("UNCHECKED_CAST")
  fun deleteCart(
      productId: String,
      cookie: String
  ) {
    callback = AppCallback(listener as OnAPIListener<CartResponse>)
    call = APIService.apiInterfaceCaffeMililani.deleteCart(productId, cookie)
    (call as Call<CartResponse>).enqueue(callback as AppCallback<CartResponse>)
  }

  /**
   * Calls the product list API.
   * Obtain the product list.
   */
  @Suppress("UNCHECKED_CAST")
  fun getProducts(page: Int) {
    refreshAccessToken()
    callback = ProductListCallback(listener as OnAPIListener<SampleProductListResponse>)
    call = APIService.apiInterfaceMililani.getProducts(apiKey, accessToken, page)
    (call as Call<SampleProductListResponse>).enqueue(callback as ProductListCallback)
  }
}
