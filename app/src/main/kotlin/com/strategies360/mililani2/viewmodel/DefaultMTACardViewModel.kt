package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.mtaCard.MTACardListResponse

class DefaultMTACardViewModel : ViewModel() {

    /** The LiveData for account data  */
    val liveData = MutableLiveData<Resource<MTACardListResponse>>()

    /** The API Caller */
    private var apiCaller: APICaller<MTACardListResponse>? = null

    /** Fetches a sample list from a remote server */
    fun defaultMtaCard(cardNumber: String) {
        liveData.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<MTACardListResponse>().withListener(
                    { response ->
                        // TODO: Add additional function if needed
                        liveData.postValue(Resource.success(response))
                    },
                    { error ->
                        liveData.postValue(Resource.error(error))
                    }
            )
        }

        apiCaller?.defaultMTACard(cardNumber)
    }

    fun cancelDefaultMTACArd() {
        apiCaller?.cancel()
    }
}
