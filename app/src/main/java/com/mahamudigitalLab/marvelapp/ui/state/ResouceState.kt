package com.mahamudigitalLab.marvelapp.ui.state

sealed class ResouceState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Sucess<T>(data: T): ResouceState<T>(data)
    class Error<T>(message: String, data: T? = null): ResouceState<T>(data, message)
    class Loading<T>: ResouceState<T>()
    class Empty<T>: ResouceState<T>()

}