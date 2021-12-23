package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.assessment.AssessmentResponse
import com.strategies360.mililani2.model.remote.contact.Contact
import com.strategies360.mililani2.model.remote.employment.Employment

class AssessmentViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for account data  */
    val resource = MutableLiveData<Resource<AssessmentResponse>>()

    /** The API Caller */
    private var apiCaller: APICaller<AssessmentResponse>? = null

    /** Fetches a sample list from a remote server */
    fun getAssessment() {
        resource.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<AssessmentResponse>().withListener(
                    { response ->
                        // TODO: Add additional function if needed
                        resource.postValue(Resource.success(response))
                    },
                    { error ->
                        resource.postValue(Resource.error(error))
                    }
            )
        }

        apiCaller?.getAssessment()
    }

    fun cancelAPI() {
        apiCaller?.cancel()
    }
}
