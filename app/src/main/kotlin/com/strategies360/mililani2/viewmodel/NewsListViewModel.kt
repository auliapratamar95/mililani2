package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.*
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.api.APIService
import com.strategies360.mililani2.api.util.CustomObserver
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.news.News
import com.strategies360.mililani2.model.remote.news.NewsResponse
import com.strategies360.mililani2.model.remote.reservation.recCenters.RecCenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<Any>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<List<News>>()

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
    fun fetchFromRemote() {
        resource.value = Resource.loading()
        val apiService = APIService.apiCustomNewsletterInterface
        apiService.getNews()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CustomObserver<Any>() {
                override fun onSuccess(response: Any) {
                    val dataResponse = response as List<News>
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

