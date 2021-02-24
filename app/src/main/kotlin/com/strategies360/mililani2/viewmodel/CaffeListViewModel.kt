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
import com.strategies360.mililani2.model.remote.caffe.Caffe
import com.strategies360.mililani2.model.remote.caffe.PayloadResponse
import com.strategies360.mililani2.model.remote.caffe.ProductCaffe
import com.strategies360.mililani2.model.remote.caffe.ProductCaffeDetail
import com.strategies360.mililani2.model.remote.caffe.ProductCaffeResponse
import com.strategies360.mililani2.model.remote.caffe.SubProductCaffe
import com.strategies360.mililani2.util.Constant

class CaffeListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<PayloadResponse>>()
    val resourceProductCaffe = MutableLiveData<Resource<ProductCaffeResponse>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<ArrayList<Caffe>>()
    val dataProductCaffeList = MutableLiveData<ArrayList<ProductCaffe>>()
    val dataProductList = MutableLiveData<ArrayList<ProductCaffeDetail>>()
    val dataSubProductCaffeList = MutableLiveData<ArrayList<SubProductCaffe>>()

    /** The API Caller */
    private var apiCaller: APICaller<PayloadResponse>? = null
    private var apiProductCaffeCaller: APICaller<ProductCaffeResponse>? = null

    /** The pagination variable */
    private var page = 1

    /** Determines if this view model has completed **ALL** of its fetching process */
    var isLoadFinished = false
        private set

    init {
        dataList.value = ArrayList()
    }

    fun fetchData(categoryId: String, page: Int, size: Int) {
        fetchFromRemote(categoryId, page, size)
    }

    fun onRefresh() {
        page = 1
        isLoadFinished = false
        dataList.value = ArrayList()
        resource.value = Resource.success()
    }

    /** Fetches a sample list from a remote server */
    private fun fetchFromRemote(categoryId: String, page: Int, size: Int) {
        resource.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<PayloadResponse>().withListener(
                    { response ->
                        if (response.payload != null) {
                            dataList.postValue(response.payload!!.caffeListResponse)
                            resource.postValue(Resource.success())
                        } else {
                            resource.postValue(Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!")))
                        }
                    },
                    { error ->
                        resource.postValue(Resource.error(error))
                    }
            )
        }

        apiCaller?.getCaffeAll(categoryId, page, size)
    }

    /** Fetches a sample list from a remote server */
    fun fetchProductCaffe() {
        resource.value = Resource.loading()

        if (apiProductCaffeCaller == null) {
            apiProductCaffeCaller = APICaller<ProductCaffeResponse>().withListener(
                { response ->
                    if (response.caffeListResponse != null) {
                        dataProductCaffeList.postValue(response.caffeListResponse)
                        resourceProductCaffe.postValue(Resource.success(response))
                    } else {
                        resourceProductCaffe.postValue(Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!")))
                    }
                },
                { error ->
                    resource.postValue(Resource.error(error))
                }
            )
        }

        apiProductCaffeCaller?.getProductCaffe()
    }

    /**
     * Fetches a sample list from local storage.
     * For this example, app will generate fixed samples.
     */
    fun fetchProductFromLocal() {
        resource.value = Resource.loading()
        if (Hawk.contains(Constant.PRODUCT_CAFFE_LIST)) {
            val productCaffe: ArrayList<ProductCaffe> = Hawk.get(Constant.PRODUCT_CAFFE_LIST)
            if (Hawk.contains(Constant.KEY_ID_CATEGORY)) {
                val categoryId: String = Hawk.get(Constant.KEY_ID_CATEGORY)
                for (i in productCaffe.indices) {
                    if (categoryId == productCaffe[i].id) {
                        dataProductList.value = productCaffe[i].productCaffeDetailList
                        resourceProductCaffe.value = Resource.success()
                    }
                }
            } else {
                for (i in productCaffe.indices) {
                    if (i == 0) {
                        dataProductList.value = productCaffe[i].productCaffeDetailList
                        resourceProductCaffe.value = Resource.success()
                    }
                }
            }
        }
    }

    /**
     * Fetches a sample list from local storage.
     * For this example, app will generate fixed samples.
     */
    fun fetchSubProductFromLocal() {
        resource.value = Resource.loading()
        if (Hawk.contains(Constant.PRODUCT_CAFFE_LIST)) {
            val productCaffe: ArrayList<ProductCaffe> = Hawk.get(Constant.PRODUCT_CAFFE_LIST)
            if (Hawk.contains(Constant.KEY_ID_CATEGORY)) {
                val categoryId: String = Hawk.get(Constant.KEY_ID_CATEGORY)
                for (i in productCaffe.indices) {
                    if (categoryId == productCaffe[i].id) {
                        dataSubProductCaffeList.value = productCaffe[i].subProductCaffeList
                        resourceProductCaffe.value = Resource.success()
                    }
                }
            } else {
                for (i in productCaffe.indices) {
                    if (i == 0) {
                        dataSubProductCaffeList.value = productCaffe[i].subProductCaffeList
                        resourceProductCaffe.value = Resource.success()
                    }
                }
            }

        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun cancel() {
        apiCaller?.cancel()
    }
}

