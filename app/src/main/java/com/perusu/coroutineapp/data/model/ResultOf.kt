package com.perusu.coroutineapp.data.model

sealed class ResultOf<out T> {
    data class Progress(val loading: Boolean = false): ResultOf<Nothing>()
    data class Success<out R>(val value: R): ResultOf<R>()
    data class Empty(val message: String): ResultOf<Nothing>()
    data class Failure(
        val message: String?,
        val throwable: Throwable?
    ): ResultOf<Nothing>()
}