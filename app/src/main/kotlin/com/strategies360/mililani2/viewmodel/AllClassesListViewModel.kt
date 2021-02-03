package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.mtaCard.Classes
import com.strategies360.mililani2.model.remote.mtaCard.ClassesListResponse

class AllClassesListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<Any>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<ArrayList<Classes>>()

    /** The API Caller */
    private var apiCaller: APICaller<ClassesListResponse>? = null

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
            apiCaller = APICaller<ClassesListResponse>().withListener(
                    { response ->
                        if (response.classListResponse != null) {
                            dataList.postValue(response.classListResponse)
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

        apiCaller?.getClassAll()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun cancel() {
        apiCaller?.cancel()
    }
}

