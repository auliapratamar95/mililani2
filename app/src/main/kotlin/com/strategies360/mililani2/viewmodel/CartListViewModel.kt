package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.DateResponse
import com.strategies360.mililani2.model.remote.caffe.cart.CartResponse
import com.strategies360.mililani2.model.remote.caffe.cart.OrderItems
import com.strategies360.mililani2.model.remote.caffe.store.StoreResponse
import com.strategies360.mililani2.util.Constant
import java.text.SimpleDateFormat
import java.util.Date

class CartListViewModel : ViewModel(), LifecycleObserver {

  /** The LiveData for resource state */
  val resource = MutableLiveData<Resource<CartResponse>>()

  /** The LiveData for list of sample products */
  val dataList = MutableLiveData<ArrayList<OrderItems>>()

  /** The API Caller */
  private var apiCaller: APICaller<CartResponse>? = null

  private var storeApiCaller: APICaller<StoreResponse>? = null

  private var dateApiCaller: APICaller<DateResponse>? = null

  /** The pagination variable */
  private var page = 1

  /** Determines if this view model has completed **ALL** of its fetching process */
  var isLoadFinished = false
    private set

  init {
    dataList.value = ArrayList()
  }

  fun fetchData(cookie: String) {
    fetchStoreFromRemote(cookie)
  }

  fun onRefresh() {
    page = 1
    isLoadFinished = false
    dataList.value = ArrayList()
    resource.value = Resource.success()
  }

  /** Fetches a sample list from a remote server */
  private fun fetchCartFromRemote(cookie: String) {
    resource.value = Resource.loading()

    if (apiCaller == null) {
      apiCaller = APICaller<CartResponse>().withListener(
          { response ->
              if (response.cart != null) {
                  dataList.postValue(response.cart!!.orderItems)
                  resource.postValue(Resource.success(response))
              } else {
                  resource.postValue(
                      Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!"))
                  )
              }
          },
          { error ->
              resource.postValue(Resource.error(error))
          }
      )
    }

    apiCaller?.getCart(cookie)
  }

  /** Fetches a sample list from a remote server */
  private fun fetchStoreFromRemote(cookie: String) {
    resource.value = Resource.loading()
    if (storeApiCaller == null) {
      storeApiCaller = APICaller<StoreResponse>().withListener(
          { response ->
              if (response.storeResponse != null) {
                  val sdf = SimpleDateFormat("yyyy-MM-dd")
                  val currentDate = sdf.format(Date())
                  fetchDateFromRemote(
                      cookie, response.storeResponse!![0].id.toString(), currentDate
                  )
              }
          }, {}
      )
    }
    storeApiCaller?.getStore(cookie)
  }

  /** Fetches a sample list from a remote server */
  private fun fetchDateFromRemote(
      cookie: String,
      storeId: String,
      date: String
  ) {
    resource.value = Resource.loading()
    if (dateApiCaller == null) {
      dateApiCaller = APICaller<DateResponse>().withListener(
          { response ->
              if (response.dateResponse != null) {
                  Hawk.delete((Constant.KEY_PICKUP_TIME))
                  Hawk.put((Constant.KEY_PICKUP_TIME), response.dateResponse)
                  fetchCartFromRemote(cookie)
              }
          }, {}
      )
    }
    dateApiCaller?.getDate(cookie, storeId, date)
  }

  /** Fetches a sample list from a remote server */
  fun deleteCartFromRemote(
      productId: String,
      cookie: String
  ) {
    resource.value = Resource.loading()

    if (apiCaller == null) {
      apiCaller = APICaller<CartResponse>().withListener(
          { response ->
              if (response.cart != null) {
                  fetchCartFromRemote(cookie)
              } else {
                  resource.postValue(
                      Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!"))
                  )
              }
          },
          { error ->
              resource.postValue(Resource.error(error))
          }
      )
    }

    apiCaller?.deleteCart(productId, cookie)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  internal fun cancel() {
    apiCaller?.cancel()
  }
}

