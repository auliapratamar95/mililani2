package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.*
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.caffe.CategoryListResponse
import com.strategies360.mililani2.model.remote.caffe.CategoryProduct

class CategoryListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<CategoryListResponse>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<ArrayList<CategoryProduct>>()

    /** The API Caller */
    private var apiCaller: APICaller<CategoryListResponse>? = null

    /** The pagination variable */
    private var page = 1

    /** Determines if this view model has completed **ALL** of its fetching process */
    var isLoadFinished = false
        private set

    init {
        dataList.value = ArrayList()
    }

    fun fetchData() {
        fetchCategoryFromRemote()
    }

    fun onRefresh() {
        page = 1
        isLoadFinished = false
        dataList.value = ArrayList()
        resource.value = Resource.success()
        fetchData()
    }

    /** Fetches a sample list from a remote server */
    fun fetchCategoryFromRemote() {
        resource.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<CategoryListResponse>().withListener(
                    { response ->
                        if (response.categoryListResponse != null) {
                            dataList.postValue(response.categoryListResponse)
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

