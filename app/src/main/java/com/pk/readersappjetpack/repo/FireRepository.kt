package com.pk.readersappjetpack.repo

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.pk.readersappjetpack.data.DataOrException
import com.pk.readersappjetpack.model.MBook
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(private val query: Query) {
	suspend fun getAllBooksFromDB(): DataOrException<List<MBook>, Boolean, Exception> {
		val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()
		try {
			dataOrException.loading = true
			dataOrException.data = query.get().await().documents.map { docSnapShot ->
				docSnapShot.toObject(MBook::class.java)!!
			}
			if (dataOrException.data!!.isEmpty()) dataOrException.loading = false
		} catch (e: FirebaseFirestoreException) {
			dataOrException.e = e
		}
		return dataOrException
	}
}