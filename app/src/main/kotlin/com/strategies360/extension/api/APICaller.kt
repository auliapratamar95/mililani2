package com.strategies360.extension.api

import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.api.util.OnAPIListener
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.AppResponse

/** Allows quick listener setup */
fun <RESPONSE : AppResponse> APICaller<RESPONSE>.withListener(onApiSuccess: ((response: RESPONSE) -> Unit)?, onApiFailure: ((error: AppError) -> Unit)?): APICaller<RESPONSE> {
    listener = object : OnAPIListener<RESPONSE> {
        override fun onApiSuccess(response: RESPONSE) {
            onApiSuccess?.invoke(response)
        }

        override fun onApiFailure(error: AppError) {
            onApiFailure?.invoke(error)
        }
    }
    return this
}
