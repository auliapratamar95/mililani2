package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.api.APIService
import com.strategies360.mililani2.api.util.CustomObserver
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.newsletter.NewsLetterResponse
import com.strategies360.mililani2.model.remote.newsletter.Newsletter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsletterViewModel : ViewModel(), LifecycleObserver {

    val resource = MutableLiveData<Resource<Any>>()

    val dataList = MutableLiveData<List<Newsletter>>()

    /** The API Caller */
    private var apiCaller: APICaller<NewsLetterResponse>? = null

    /** Determines if this view model has completed **ALL** of its fetching process */
    var isLoadFinished = false
        private set

    init {
        dataList.value = ArrayList()
    }

    fun fetchData() {
        newsLetterAPI()
    }

    fun onRefresh() {
        isLoadFinished = false
        dataList.value = ArrayList()
        resource.value = Resource.success()
        fetchData()
    }

    private fun newsLetterAPI() {
        resource.value = Resource.loading()
        val apiService = APIService.apiCustomNewsletterInterface
        apiService.getNewsletter("12")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CustomObserver<Any>() {
                override fun onSuccess(response: Any) {
                    val dataResponse = response as List<Newsletter>
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

    fun cancelAPI() {
        apiCaller?.cancel()
    }
}
