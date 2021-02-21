package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.*
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.caffe.Caffe
import com.strategies360.mililani2.model.remote.caffe.CaffeListResponse
import com.strategies360.mililani2.model.remote.caffe.PayloadResponse

class CaffeListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<PayloadResponse>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<ArrayList<Caffe>>()

    /** The API Caller */
    private var apiCaller: APICaller<PayloadResponse>? = null

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
//        fetchData()
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
    fun fetchCategoryFromRemote() {
        resource.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<PayloadResponse>().withListener(
                    { response ->
                        if (response.payload != null) {
                            dataList.postValue(response.payload!!.




                            caffeListResponse)
                            resource.postValue(Resource.success(response))
                        } else {
                            resource.postValue(Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!")))
                        }
                    },
                    { error ->
                        resource.postValue(Resource.error(error))
                    }
            )
        }

        apiCaller?.getCategory()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun cancel() {
        apiCaller?.cancel()
    }
}

