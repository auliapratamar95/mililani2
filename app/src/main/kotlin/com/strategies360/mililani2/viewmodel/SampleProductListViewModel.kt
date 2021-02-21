package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.*
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.product.SampleProduct
import com.strategies360.mililani2.model.remote.product.SampleProductListResponse

class SampleProductListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<Any>>()

    /** The LiveData for list of sample products */
    val dataList = MutableLiveData<List<SampleProduct>>()

    /** The API Caller */
    private var apiCaller: APICaller<SampleProductListResponse>? = null

    /** The pagination variable */
    private var page = 1

    /** Determines if this view model has successfully fetch a data from local storage */
    private var hasFetchedFromLocal = false

    /** Determines if this view model has completed **ALL** of its fetching process */
    var isLoadFinished = false
        private set

    init {
        dataList.value = ArrayList()
    }

    fun fetchData() {
        when (hasFetchedFromLocal) {
            true -> fetchFromRemote()
            else -> fetchFromLocal()
        }
    }

    fun onRefresh() {
        page = 1
        hasFetchedFromLocal = false
        isLoadFinished = false
        dataList.value = ArrayList()
        resource.value = Resource.success()
        fetchData()
    }

    /**
     * Fetches a sample list from local storage.
     * For this example, app will generate fixed samples.
     */
    private fun fetchFromLocal() {
        resource.value = Resource.loading()

        val newList = ArrayList<SampleProduct>(dataList.value!!)
        var product: SampleProduct
        for (i in 0..9) {
            product = SampleProduct()
            product.id = i
            product.name = "local product $i"
            newList.add(product)
        }
        dataList.value = newList
        resource.value = Resource.success()
        hasFetchedFromLocal = true
    }

    /** Fetches a sample list from a remote server */
    private fun fetchFromRemote() {
        resource.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<SampleProductListResponse>().withListener(
                    { response ->
                        // Set your condition to handle the end of infinite scroll behaviour
                        page++
                        if (page > 5) isLoadFinished = true
                        if (response.productList != null) {
                            // Modify each product ID to make it unique on the list
                            // This is added because the local data and apiary data has the same IDs,
                            // which will cause the RecyclerView to show a buggy behaviour
                            var id = 100 * (page - 1)

                            val newList = ArrayList<SampleProduct>()
                            var product: SampleProduct
                            for (i in 0..9) {
                                product = SampleProduct()
                                product.id = id
                                product.name = "fake remote product $i"
                                newList.add(product)
                                id++
                            }
                            for (remoteProduct in response.productList!!) {
                                remoteProduct.id = id
                                newList.add(remoteProduct)
                                id++
                            }

                            val newDataList = ArrayList(dataList.value!!)
                            newDataList.addAll(newList)
                            dataList.postValue(newDataList)
                            resource.postValue(Resource.success())
                        } else {
                            resource.postValue(Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!")))
                        }
                    },
                    { error ->
                        resource.postValue(Resource.error(error))
                    }
            )
        }

        apiCaller?.getProducts(page)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun cancel() {
        apiCaller?.cancel()
    }
}

