package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.*
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.api.APIService
import com.strategies360.mililani2.api.util.CustomObserver
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.reservation.recCenters.RecCenter
import com.strategies360.mililani2.model.remote.reservation.recCenters.RecCentersResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecCenterListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<RecCentersResponse>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<List<RecCenter>>()

    /** The API Caller */
    private var apiCaller: APICaller<RecCentersResponse>? = null

    /** The pagination variable */
    private var page = 1

    /** Determines if this view model has completed **ALL** of its fetching process */
    var isLoadFinished = false
        private set

    init {
        dataList.value = ArrayList()
    }

    fun onRefresh() {
        page = 1
        isLoadFinished = false
        dataList.value = ArrayList()
        resource.value = Resource.success()
    }

    /** Fetches a sample list from a remote server */
    fun fetchFromRemote() {
        resource.value = Resource.loading()
        val apiService = APIService.apiCustomNewsletterInterface
        apiService.getRecCenters("asc")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : CustomObserver<Any>() {
                    override fun onSuccess(response: Any) {
                        val dataResponse = response as List<RecCenter>
                        if (dataResponse.isNotEmpty()) {
                            resource.postValue(Resource.success())
                            dataList.postValue(dataResponse)
                        }
                    }

                    override fun onFailure(error: Any) {
                        resource.postValue(Resource.success())
                    }

                })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun cancel() {
        apiCaller?.cancel()
    }
}

