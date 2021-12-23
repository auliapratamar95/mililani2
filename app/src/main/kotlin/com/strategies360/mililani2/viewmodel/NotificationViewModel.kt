package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.*
import com.orhanobut.hawk.Hawk
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.api.APIService
import com.strategies360.mililani2.api.util.CustomObserver
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.news.News
import com.strategies360.mililani2.model.remote.notification.Notification
import com.strategies360.mililani2.model.remote.notification.NotificationResponse
import com.strategies360.mililani2.util.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NotificationViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<Any>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<List<Notification>>()

    /** The API Caller */
    private var apiCaller: APICaller<NotificationResponse>? = null

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
//    private fun fetchFromRemote() {
//        resource.value = Resource.loading()
//
//        if (apiCaller == null) {
//            apiCaller = APICaller<NotificationResponse>().withListener(
//                    { response ->
//                        if (response.notificationResponse != null) {
//                            dataList.postValue(response.notificationResponse)
//                            resource.postValue(Resource.success())
//                        } else {
//                            resource.postValue(Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!")))
//                        }
//                    },
//                    { error ->
//                        resource.postValue(Resource.error(error))
//                    }
//            )
//        }
//
//        apiCaller?.getNotification()
//    }

    fun fetchFromRemote() {
        var accessToken = ""
        if (Hawk.contains(Constant.KEY_TOKEN)) accessToken = "Bearer " + Hawk.get(Constant.KEY_TOKEN)
        resource.value = Resource.loading()
        val apiService = APIService.apiCustomNewsletterInterface
        apiService.getNotification(accessToken, "10")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CustomObserver<Any>() {
                override fun onSuccess(response: Any) {
                    val dataResponse = response as List<Notification>
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

