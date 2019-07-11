package com.esh1n.core_android.retrofit

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException


class RetrofitImageUtil {
    companion object {

        fun tryConvertImage(serverName: String, path: String?): MultipartBody.Part {
            return if (path == null) createEmpty() else convertImage(serverName, path)
        }

        @Throws(IOException::class)
        private fun convertImage(serverName: String, path: String): MultipartBody.Part {
            val file = File(path)
            return convertImage(serverName, file)
        }

        @Throws(IOException::class)
        fun convertImage(serverName: String, file: File): MultipartBody.Part {
            val fileName = file.name
            val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
            return MultipartBody.Part.createFormData(serverName, fileName, reqFile)
        }

        private fun createEmpty(): MultipartBody.Part {
            val attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "")
            return MultipartBody.Part.createFormData("attachment", "", attachmentEmpty)
        }
    }
}