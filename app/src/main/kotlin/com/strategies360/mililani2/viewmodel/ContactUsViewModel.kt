package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.contact.Contact

class ContactUsViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for account data  */
    val resource = MutableLiveData<Resource<Contact>>()

    /** The API Caller */
    private var apiCaller: APICaller<Contact>? = null

    /** Fetches a sample list from a remote server */
    fun sample() {
        resource.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<Contact>().withListener(
                    { response ->
                        // TODO: Add additional function if needed
                        resource.postValue(Resource.success(response))
                    },
                    { error ->
                        resource.postValue(Resource.error(error))
                    }
            )
        }

        apiCaller?.getContactUs()
    }

    fun cancelAPI() {
        apiCaller?.cancel()
    }
}
