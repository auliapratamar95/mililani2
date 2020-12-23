package com.strategies360.mililani2.model.local.util

import android.net.Uri

import com.google.gson.annotations.SerializedName

class ImageCropParams {

    @SerializedName("img_source")
    var sourceImage = Uri.EMPTY

    @SerializedName("img_destination")
    var destinationImage = Uri.EMPTY

    @SerializedName("aspect_w")
    var aspectWidth = -1

    @SerializedName("aspect_h")
    var aspectHeight = -1

    @SerializedName("min_w")
    var minWidth = -1

    @SerializedName("min_h")
    var minHeight = -1

    @SerializedName("max_w")
    var maxWidth = -1

    @SerializedName("max_h")
    var maxHeight = -1
}
