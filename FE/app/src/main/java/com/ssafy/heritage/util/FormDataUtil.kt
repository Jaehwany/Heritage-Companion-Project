
package com.ssafy.heritage.util


import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object FormDataUtil {

    fun getBody(key: String, value: Any): MultipartBody.Part {
        return MultipartBody.Part.createFormData(key, value.toString())
    }


    fun getImageBody(key: String, file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = key,
            filename = file.name.replace("%",""),
            body = file.asRequestBody("image/*".toMediaTypeOrNull())
        )
    }

    fun getVideoBody(key: String, file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = key,
            filename = file.name,
            body = file.asRequestBody("video/*".toMediaType())
        )
    }
}