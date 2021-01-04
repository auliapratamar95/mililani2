package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.auth.SignInMililaniResponse

class SubmitMTACardViewModel : ViewModel() {

    /** The LiveData for account data  */
    val liveData = MutableLiveData<Resource<SignInMililaniResponse>>()

    /** The API Caller */
    private var apiCaller: APICaller<SignInMililaniResponse>? = null

    /** Fetches a sample list from a remote server */
    fun signInMililani(request: String) {
        liveData.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<SignInMililaniResponse>().withListener(
                    { response ->
                        // TODO: Add additional function if needed
                        liveData.postValue(Resource.success(response))
                    },
                    { error ->
                        liveData.postValue(Resource.error(error))
                    }
            )
        }

        apiCaller?.submitMTACard(request)
    }

    fun cancelSubmitMTACArd() {
        apiCaller?.cancel()
    }
}
