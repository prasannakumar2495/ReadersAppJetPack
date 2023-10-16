package com.pk.readersappjetpack.repo

import com.pk.readersappjetpack.data.DataOrException
import com.pk.readersappjetpack.model.Item
import com.pk.readersappjetpack.network.BooksApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BooksRepository @Inject constructor(private val api: BooksApi) {
	private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
	private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()
	suspend fun getBooks(query: String): Flow<DataOrException<List<Item>, Boolean, Exception>> =
		flow {
			try {
				dataOrException.loading = true
				dataOrException.data = api.getBooks(query).items
				if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false
			} catch (e: Exception) {
				dataOrException.e = e
			}
			emit(dataOrException)
		}.flowOn(Dispatchers.IO)
	
	suspend fun getBookInfo(bookId: String): Flow<DataOrException<Item, Boolean, Exception>> =
		flow {
			try {
				bookInfoDataOrException.loading = true
				bookInfoDataOrException.data = api.getBookInfo(bookId)
				if (bookInfoDataOrException.data.toString()
						.isNotEmpty()
				) bookInfoDataOrException.loading = false
			} catch (e: Exception) {
				bookInfoDataOrException.e = e
			}
			emit(bookInfoDataOrException)
		}.flowOn(Dispatchers.IO)
}