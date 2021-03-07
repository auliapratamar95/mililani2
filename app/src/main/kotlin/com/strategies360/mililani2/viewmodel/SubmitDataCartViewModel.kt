package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.caffe.PayloadResponse
import com.strategies360.mililani2.model.remote.caffe.cart.CartRequest

class SubmitDataCartViewModel : ViewModel() {

    /** The LiveData for account data  */
    val liveData = MutableLiveData<Resource<PayloadResponse>>()

    /** The API Caller */
    private var apiCaller: APICaller<PayloadResponse>? = null

    /** Fetches a sample list from a remote server */
    fun submitDataCart(request: CartRequest) {
        liveData.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<PayloadResponse>().withListener(
                    { response ->
                        // TODO: Add additional function if needed
                        liveData.postValue(Resource.success(response))
                    },
                    { error ->
                        liveData.postValue(Resource.error(error))
                    }
            )
        }

        apiCaller?.submitDataCart(request)
    }

    fun cancelSubmitMTACArd() {
        apiCaller?.cancel()
    }
}
