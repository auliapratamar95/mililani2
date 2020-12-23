package com.strategies360.mililani2.api.callback

import com.strategies360.mililani2.api.util.OnAPIListener
import com.strategies360.mililani2.api.callback.core.CoreCallback
import com.strategies360.mililani2.model.core.AppResponse

import retrofit2.Response

/**
 *
 * The default callback for the application.
 */
class AppCallback<RESPONSE: AppResponse>(listener: OnAPIListener<RESPONSE>?) : CoreCallback<RESPONSE>(listener) {

    override fun onSuccess(response: Response<RESPONSE>) {
        handleSuccess(response)
    }

    override fun onFailure(responseCode: Int, errorMessage: String) {
        handleFailure(responseCode, errorMessage)
    }
}
