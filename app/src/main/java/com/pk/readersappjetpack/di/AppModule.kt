package com.pk.readersappjetpack.di

import com.google.firebase.firestore.FirebaseFirestore
import com.pk.readersappjetpack.network.BooksApi
import com.pk.readersappjetpack.repo.BooksRepository
import com.pk.readersappjetpack.repo.FireRepository
import com.pk.readersappjetpack.util.Constants.BASE_URL
import com.pk.readersappjetpack.util.Constants.SAVED_BOOKS_COLLECTION
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.grpc.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
	@Singleton
	@Provides
	fun provideBookApi(): BooksApi = Retrofit.Builder()
		.baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
		.build().create(BooksApi::class.java)
	
	@Singleton
	@Provides
	fun provideBookRepository(api: BooksApi) = BooksRepository(api)
	
	@Singleton
	@Provides
	fun provideFireBookRepository() = FireRepository(
		query = FirebaseFirestore.getInstance()
			.collection(SAVED_BOOKS_COLLECTION)
	)
}