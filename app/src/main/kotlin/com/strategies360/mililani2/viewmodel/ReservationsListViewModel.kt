package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.*
import com.orhanobut.hawk.Hawk
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.reservation.Reservation
import com.strategies360.mililani2.model.remote.reservation.ReservationsResponse
import com.strategies360.mililani2.model.remote.tickets.Events
import com.strategies360.mililani2.model.remote.tickets.EventsResponse
import com.strategies360.mililani2.util.Constant

class ReservationsListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<ReservationsResponse>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<ArrayList<Reservation>>()

    /** The API Caller */
    private var apiCaller: APICaller<ReservationsResponse>? = null

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
    fun fetchFromRemote(facilityLocation: String,
                        facilityClass: String) {
        resource.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<ReservationsResponse>().withListener(
                    { response ->
                        if (response.reservationList != null) {
                            dataList.postValue(response.reservationList)
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

        apiCaller?.getReservations(facilityLocation, facilityClass)
    }

    fun fetchFromLocal() {
        resource.value = Resource.loading()
        val dataListEvents: ArrayList<Reservation> = Hawk.get(Constant.KEY_RESERVATION_LIST)
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

