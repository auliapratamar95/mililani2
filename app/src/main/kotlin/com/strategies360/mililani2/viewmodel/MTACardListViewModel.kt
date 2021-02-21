package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.*
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.mtaCard.MTACard
import com.strategies360.mililani2.model.remote.mtaCard.MTACardListResponse

class MTACardListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<Any>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<ArrayList<MTACard>>()

    /** The API Caller */
    private var apiCaller: APICaller<MTACardListResponse>? = null

    /** The pagination variable */
    private var page = 1

    /** Determines if this view model has completed **ALL** of its fetching process */
    var isLoadFinished = false
        private set

    init {
        dataList.value = ArrayList()
    }

    fun fetchData() {
        fetchFromRemote()
    }

    fun onRefresh() {
        page = 1
        isLoadFinished = false
        dataList.value = ArrayList()
        resource.value = Resource.success()
        fetchData()
    }

    /** Fetches a sample list from a remote server */
    private fun fetchFromRemote() {
        resource.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<MTACardListResponse>().withListener(
                    { response ->
                        if (response.mtaCarfList != null) {
                            dataList.postValue(response.mtaCarfList)
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

        apiCaller?.getMTACard()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun cancel() {
        apiCaller?.cancel()
    }
}

