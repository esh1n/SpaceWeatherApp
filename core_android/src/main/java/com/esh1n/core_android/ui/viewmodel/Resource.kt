package com.esh1n.core_android.ui.viewmodel

import com.esh1n.core_android.ui.viewmodel.Resource.Status.SUCCESS

data class Resource<T>(val status: Status, val data: T?, val message: String) {


    enum class Status {
        SUCCESS, ERROR, LOADING, ENDED
    }

    companion object {


        fun success(): Resource<Unit> {
            return Resource(SUCCESS, Unit, "")
        }

        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, "")
        }

        fun <T, R> success(data: Resource<R>, func: (R?)->T): Resource<T> {
            return Resource(SUCCESS, func.invoke(data.data), data.message)
        }

        fun <T> error(msg: String): Resource<T> {
            return Resource(
                Status.ERROR,
                null,
                msg
            )
        }

        fun <S,R> map(resource: Resource<S>, mapper: (S)->R): Resource<R> {
            val data = resource.data
            if(resource.status== SUCCESS){
                val mappedData = if(data!=null) mapper.invoke(data) else data
                return Resource(SUCCESS, mappedData, resource.message)
            }
            return copyWithNullData(resource)
        }

        fun <S,R> copyWithNullData(resource: Resource<S>):Resource<R>{
            return Resource(resource.status,null ,resource.message)
        }

        fun <T> loading(): Resource<T> {
            return Resource(
                Status.LOADING,
                null,
                ""
            )
        }

        fun <T> ended(): Resource<T> {
            return Resource(
                Status.ENDED,
                null,
                ""
            )
        }
    }
}
