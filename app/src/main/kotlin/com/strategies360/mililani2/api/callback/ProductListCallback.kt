package com.strategies360.mililani2.api.callback

import com.strategies360.mililani2.api.callback.core.CoreCallback
import com.strategies360.mililani2.api.util.OnAPIListener
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.remote.product.SampleProductListResponse
import com.strategies360.mililani2.util.Debugger
import retrofit2.Response

/**
 *
 * A sample custom callback for product list.
 */
class ProductListCallback(listener: OnAPIListener<SampleProductListResponse>) : CoreCallback<SampleProductListResponse>(listener) {

    override fun onSuccess(response: Response<SampleProductListResponse>) {
        // Do heavy job in a different thread,
        // e.g. Store the obtained data list into local table, etc.
        val thread = Thread {
            try {
                try {
                    // Simulate a long process
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    Debugger.logException(e)
                }
                callListenerSuccess(response.body())
            } catch (e: ClassCastException) {
                Debugger.logException(e)
                callListenerFailure(AppError(response.code(), e.message))
            }
        }

        // Execute the heavy job
        thread.start()
    }

    override fun onFailure(responseCode: Int, errorMessage: String) {
        handleFailure(responseCode, errorMessage)
    }
}
