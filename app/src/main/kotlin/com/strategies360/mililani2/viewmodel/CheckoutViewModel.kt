package com.strategies360.mililani2.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import com.strategies360.extension.api.withListener
import com.strategies360.mililani2.App
import com.strategies360.mililani2.api.APICaller
import com.strategies360.mililani2.api.APIService
import com.strategies360.mililani2.api.util.CustomObserver
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.caffe.checkout.BillingAddress
import com.strategies360.mililani2.model.remote.caffe.checkout.BillingRequest
import com.strategies360.mililani2.model.remote.caffe.checkout.CardInfo
import com.strategies360.mililani2.model.remote.caffe.checkout.MonetraResp
import com.strategies360.mililani2.model.remote.caffe.checkout.Options
import com.strategies360.mililani2.model.remote.caffe.checkout.PayTicket
import com.strategies360.mililani2.model.remote.caffe.checkout.PayTicketResponse
import com.strategies360.mililani2.model.remote.caffe.checkout.PickupRequest
import com.strategies360.mililani2.model.remote.caffe.checkout.PickupResponse
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CheckoutViewModel : ViewModel(), LifecycleObserver {

  /** The LiveData for resource state */
  val resourcePickup = MutableLiveData<Resource<Any>>()

  val resourcePayTicket = MutableLiveData<Resource<PayTicket>>()

  val resourceSecurePayment = MutableLiveData<Resource<Any>>()

  /** The API Caller */
  private var apiPickupCaller: APICaller<PickupResponse>? = null

  private var apiPayTicketCaller: APICaller<PayTicketResponse>? = null

  private var apiBilingCaller: APICaller<PickupResponse>? = null

//  private var apiSecurePaymentCaller: APICaller<Sample>? = null

  private var tmpCookies = ""
  private var tip = 0.0

  /** The pagination variable */
  private var page = 1

  /** Determines if this view model has completed **ALL** of its fetching process */
  var isLoadFinished = false
    private set

  fun onRefresh() {
    page = 1
    isLoadFinished = false
    resourcePickup.value = Resource.success()
    resourcePayTicket.value = Resource.success()
    resourceSecurePayment.value = Resource.success()
  }

  /** Fetches a sample list from a remote server */
  fun fetchPickupFromRemote(cookies: String, pickupRequest: PickupRequest, tip: Double) {
    resourcePickup.value = Resource.loading()
    this.tip = tip
    if (apiPickupCaller == null) {
      apiPickupCaller = APICaller<PickupResponse>().withListener(
          { response ->
            if (response.pickupResponse != null) {
              resourcePickup.postValue(Resource.success(cookies))
//              fetchPayTicketFromRemote(cookies)
            } else {
              resourcePickup.postValue(Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!")))
            }
          },
          { error ->
            resourcePickup.postValue(Resource.error(error))
          }
      )
    }

    apiPickupCaller?.putPickup(cookies, pickupRequest)
  }

  fun fetchPayTicketFromRemote(cookies: String) {
    resourcePayTicket.value = Resource.loading()
    tmpCookies = cookies
    if (apiPayTicketCaller == null) {
      apiPayTicketCaller = APICaller<PayTicketResponse>().withListener(
          { response ->
            if (response.payTicket != null) {
              resourcePayTicket.postValue(Resource.success(response.payTicket))
              Hawk.put((Constant.KEY_URL_PAYMENT), response)

            } else {
              resourcePayTicket.postValue(Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!")))
            }
          },
          { error ->
            resourcePayTicket.postValue(Resource.error(error))
          }
      )
    }

    apiPayTicketCaller?.getPayTicket(cookies)
  }

  fun fetchSecurePaymentFromRemote(userName: String, action: String, admin: String, monetraReqSequence: String,
    monetraReqTimestamp: String, monetraReqHmacsha256: String, account: String,
    cardHolderName: String, expdate: String, cv: String, zip: String, fullName: String) {
    val apiService = APIService.apiInterfaceCustomMililani
    resourceSecurePayment.postValue(Resource.loading())
    apiService.submitDataSecurePay(userName, action, admin, monetraReqSequence, monetraReqTimestamp,
        monetraReqHmacsha256, account, cardHolderName, expdate, cv, zip
    )
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(object : CustomObserver<Any>() {
          override fun onSuccess(response: Any) {
            val dataResponse = response as MonetraResp
            val status: String = dataResponse.dataTransferStatus.code
            if (status == "SUCCESS") {
              val last4DigitCardNumber = account.substring(account.length - 4)
              val resp = dataResponse.resp
              val ticket = resp.ticket
              val monetraResp = resp.monetraRespHmacsha256
              val billingRequest = BillingRequest()
              val billingAddress = BillingAddress()
              val cardInfo = CardInfo()
              val options = Options()

              val month = if (expdate.length == 3) {
                "0" + expdate.substring(0, 1)
              } else {
                expdate.substring(0, 2)
              }

              val year = expdate.substring(expdate.length - 2)

              billingAddress.addressLine1 = "-"
              billingAddress.addressLine2 = "-"
              billingAddress.city = "-"
              billingAddress.phone = "-"
              billingAddress.fullName = fullName
              billingAddress.postalCode = zip
              billingAddress.differentBilling = false

              cardInfo.name = fullName
              cardInfo.cardNumber = "************$last4DigitCardNumber"
              cardInfo.cvc = "***"
              cardInfo.last4 = last4DigitCardNumber
              cardInfo.expMonth = month.toInt()
              cardInfo.expYear = year.toInt()
              cardInfo.expirationDate = "$month/$year"

              options.hmac = monetraResp

              billingRequest.token = ticket
              billingRequest.billingAddress = billingAddress
              billingRequest.cardInfo = cardInfo
              billingRequest.options = options

              fetchBiling(tmpCookies, billingRequest)
            }

            isLoadFinished = true
          }

          override fun onFailure(error: Any) {
//                    resource.postValue(Resource.error())
          }
        })
  }

  private fun fetchBiling(cookies: String, billingRequest: BillingRequest) {
    resourcePayTicket.value = Resource.loading()

    if (apiBilingCaller == null) {
      apiBilingCaller = APICaller<PickupResponse>().withListener(
          { response ->
            fetchTip(cookies, tip)
            fetchCheckout(cookies)
//            if (response.payTicket != null) {
//              resourcePayTicket.postValue(Resource.success(response.payTicket))
//              Hawk.put((Constant.KEY_URL_PAYMENT), response)
//
//            } else {
//              resourcePayTicket.postValue(Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!")))
//            }
          },
          { error ->
            resourcePayTicket.postValue(Resource.error(error))
          }
      )
    }

    apiBilingCaller?.submitBiling(cookies, billingRequest)
  }

  private fun fetchTip(cookies: String, tip: Double) {
    resourcePayTicket.value = Resource.loading()

    if (apiBilingCaller == null) {
      apiBilingCaller = APICaller<PickupResponse>().withListener(
          { response ->
          },
          { error ->
            resourcePayTicket.postValue(Resource.error(error))
          }
      )
    }

    apiBilingCaller?.submitTip(cookies, tip)
  }

  private fun fetchCheckout(cookies: String) {
    resourcePayTicket.value = Resource.loading()

    if (apiBilingCaller == null) {
      apiBilingCaller = APICaller<PickupResponse>().withListener(
          { response ->
//            if (response.payTicket != null) {
//              resourcePayTicket.postValue(Resource.success(response.payTicket))
//              Hawk.put((Constant.KEY_URL_PAYMENT), response)
//
//            } else {
//              resourcePayTicket.postValue(Resource.error(AppError(AppError.DEVELOPMENT_UNKNOWN, "Data list is null!")))
//            }
          },
          { error ->
            Common.showMessageDialog(App.context, "Error", error.message)
            resourcePayTicket.postValue(Resource.error(error))
          }
      )
    }

    apiBilingCaller?.submitCheckout(cookies)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  internal fun cancel() {
    apiPickupCaller?.cancel()
    apiPayTicketCaller?.cancel()
//    apiSecurePaymentCaller?.cancel()
  }
}

