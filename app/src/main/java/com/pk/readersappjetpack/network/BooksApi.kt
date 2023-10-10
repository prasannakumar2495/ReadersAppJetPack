package com.pk.readersappjetpack.network

import com.pk.readersappjetpack.model.Book
import com.pk.readersappjetpack.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {
	@GET("volumes")
	suspend fun getBooks(@Query("q") query: String): Book
	
	@GET("volumes/{bookId}")
	suspend fun getBookInfo(@Path("bookId") bookId: String): Item
}