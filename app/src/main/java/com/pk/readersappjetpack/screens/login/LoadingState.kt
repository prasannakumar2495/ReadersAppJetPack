package com.pk.readersappjetpack.screens.login

//data class LoadingState(
//	val status: Status, val message: String? = null,
//) {
//	companion object {
//		val IDLE = LoadingState(Status.IDLE)
//		val LOADING = LoadingState(Status.LOADING)
//		val FAILURE = LoadingState(Status.FAILURE)
//		val SUCCESS = LoadingState(Status.SUCCESS)
//	}
//
//	enum class Status {
//		SUCCESS, LOADING, FAILURE, IDLE
//	}
//}
sealed class LoadingState<out T>(data: T?, message: String?) {
	object LOADING : LoadingState<Nothing>(null, null)
	object IDLE : LoadingState<Nothing>(null, null)
	data class FAILURE(val throwable: Throwable) :
		LoadingState<Nothing>(data = null, throwable.message)
	
	data class SUCCESS<T>(val item: T) : LoadingState<T>(data = item, null)
}