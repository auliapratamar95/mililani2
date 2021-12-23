package com.strategies360.mililani2.model.remote.employment

import com.google.gson.annotations.SerializedName
import com.strategies360.mililani2.model.core.AppResponse

class EmploymentResponse : AppResponse() {
    var employmentList: Employment? = null
}
