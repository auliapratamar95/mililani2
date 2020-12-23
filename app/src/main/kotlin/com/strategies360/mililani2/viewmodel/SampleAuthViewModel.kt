package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.auth.SignInRequest
import com.strategies360.mililani2.model.remote.auth.SignInResponse
import com.strategies360.extension.api.withListener

/**
 * Created by Alvin Rusli on 12/20/2017.
 */
class SampleAuthViewModel : ViewModel() {

    /** The LiveData for account data  */
    val liveData = MutableLiveData<Resource<SignInResponse>>()

    /** The API Caller */
    private var apiCaller: APICaller<SignInResponse>? = null

    /** Fetches a sample list from a remote server */
    fun signIn(request: SignInRequest) {
        liveData.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<SignInResponse>().withListener(
                    { response ->
                        // TODO: Add additional function if needed
                        liveData.postValue(Resource.success(response))
                    },
                    { error ->
                        liveData.postValue(Resource.error(error))
                    }
            )
        }

        apiCaller?.signIn(request)
    }

    fun cancelSignIn() {
        apiCaller?.cancel()
    }
}
