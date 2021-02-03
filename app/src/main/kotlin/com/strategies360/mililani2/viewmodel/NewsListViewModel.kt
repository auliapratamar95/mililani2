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
import com.strategies360.mililani2.model.remote.news.News
import com.strategies360.mililani2.model.remote.news.NewsResponse

class NewsListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<Any>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<ArrayList<News>>()

    /** The API Caller */
    private var apiCaller: APICaller<NewsResponse>? = null

    /** Determines if this view model has completed **ALL** of its fetching process */
    var isLoadFinished = false
        private set

    init {
        dataList.value = ArrayList()
    }

    fun fetchData() {
        fetchFromRemote()
    }

    /** Fetches a sample list from a remote server */
    private fun fetchFromRemote() {
        resource.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<NewsResponse>().withListener(
                    { response ->
                        if (response.newsList != null) {
                            dataList.postValue(response.newsList)
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

        apiCaller?.getNews()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun cancel() {
        apiCaller?.cancel()
    }
}

