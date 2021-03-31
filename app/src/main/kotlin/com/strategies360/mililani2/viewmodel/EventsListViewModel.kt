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
import com.strategies360.mililani2.model.remote.tickets.Events
import com.strategies360.mililani2.model.remote.tickets.EventsResponse
import com.strategies360.mililani2.util.Constant

class EventsListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<EventsResponse>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<ArrayList<Events>>()

    /** The API Caller */
    private var apiCaller: APICaller<EventsResponse>? = null

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
    fun fetchFromRemote(ticketCode: String,
        beginEventDate: String,
        endEventDate: String,
        key: String) {
        resource.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<EventsResponse>().withListener(
                    { response ->
                        if (response.eventsList != null) {
                            dataList.postValue(response.eventsList)
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

        apiCaller?.getTickets(ticketCode, beginEventDate, endEventDate, key)
    }

    fun fetchFromLocal() {
        resource.value = Resource.loading()
        val dataListEvents: ArrayList<Events> = Hawk.get(Constant.KEY_TICKET_LIST)
        if (dataListEvents.size != 0) {
            dataList.postValue(dataListEvents)
            resource.postValue(Resource.success())
        } else {
            resource.postValue(Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!")))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun cancel() {
        apiCaller?.cancel()
    }
}

