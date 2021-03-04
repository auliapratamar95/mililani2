package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.caffe.CategoryDetailsProductResponse
import com.strategies360.mililani2.model.remote.caffe.ModifierGroups
import com.strategies360.mililani2.model.remote.caffe.ProductOptions
import com.strategies360.mililani2.util.Constant

class CategoryDetailsProductViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<CategoryDetailsProductResponse>>()

    /** The LiveData for list of sample products */
    val dataAverageAmount = MutableLiveData<String>()

    val dataList = MutableLiveData<ArrayList<ProductOptions>>()

    val dataModifiersGroupList = MutableLiveData<ArrayList<ModifierGroups>>()

    /** The API Caller */
    private var apiCaller: APICaller<CategoryDetailsProductResponse>? = null

    /** The pagination variable */
    private var page = 1

    /** Determines if this view model has completed **ALL** of its fetching process */
    var isLoadFinished = false
        private set

    init {
        dataList.value = ArrayList()
    }

    fun fetchData(productId: String) {
        fetchCategoryFromRemote(productId)
    }

    fun onRefresh() {
        page = 1
        isLoadFinished = false
        dataList.value = ArrayList()
        resource.value = Resource.success()
//        fetchData()
    }

    /** Fetches a sample list from a remote server */
    private fun fetchCategoryFromRemote(productId: String) {
        resource.value = Resource.loading()

        if (apiCaller == null) {
            apiCaller = APICaller<CategoryDetailsProductResponse>().withListener(
                { response ->
                    if (response.categoryDetailsProductResponse != null) {
                        dataList.postValue(
                            response.categoryDetailsProductResponse!!.productOptionsList
                        )

                        dataModifiersGroupList.postValue(
                            response.categoryDetailsProductResponse!!.modifierGroupsList
                        )
                        resource.postValue(Resource.success(response))

                        if (Hawk.contains(Constant.SKU_LIST)) {
                            Hawk.delete(Constant.SKU_LIST)
                            Hawk.put((Constant.SKU_LIST), response.categoryDetailsProductResponse!!.detailSkus)
                        } else {
                            Hawk.put((Constant.SKU_LIST), response.categoryDetailsProductResponse!!.detailSkus)
                        }


//                        val gson = GsonBuilder().setPrettyPrinting().create()
//                        val issueObj = JSONObject(
//                            gson.toJson(
//                                response.categoryDetailsProductResponse
//                            )
//                        )
//                        val iterator: Iterator<*> = issueObj.keys()
//                        var savedAccount: DetailSkus
//                        //while (iterator.hasNext()) {
////                        for (i in iterator.hasNext())
//
//                            //val key = iterator.next() as String
//                            val issue: JSONObject = issueObj.getJSONObject("skus")
//                            val keys = issue.keys()
//                            val key = keys.next() as String
//                            val item: JSONObject = issue.getJSONObject(key)
//                            val detailSkus  = item.toString()
//
//                            savedAccount= Gson().fromJson(
//                                detailSkus, DetailSkus::class.java)
//
//
//                            //  get id from  issue
//                            val _pubKey: String = issue.optString("id")
                        //}

                    } else {
                        resource.postValue(
                            Resource.error(
                                AppError(
                                    AppError.DEVELOPMENT_UNKNOWN, "Data list is null!"
                                )
                            )
                        )
                    }
                },
                { error ->
                    resource.postValue(Resource.error(error))
                }
            )
        }

        apiCaller?.getCategoryDetailsProduct(productId)
    }

    private fun initAverageAmount(response: CategoryDetailsProductResponse) {
        val listSkus = response.categoryDetailsProductResponse?.detailSkus
        val listAmount: ArrayList<Double> = ArrayList()
        var amount: Double?

        if (listSkus != null) {
            for (i in listSkus.indices) {
                amount = listSkus[i].priceSkus?.amount
                if (amount != null) {
                    listAmount.add(amount)
                }
            }
        }

        if (listAmount.size != 0) {
            var maxValue: Double = listAmount[0]
            var minValue: Double = listAmount[0]
            for (i in listAmount.indices) {
                if(maxValue < listAmount[i]){
                    maxValue = listAmount[i];
                }
                if(minValue > listAmount[i]){
                    minValue = listAmount[i];
                }
            }
            dataAverageAmount.postValue( "$$minValue - $$minValue")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun cancel() {
        apiCaller?.cancel()
    }
}

