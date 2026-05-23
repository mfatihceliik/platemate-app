package com.mefy.platemate.data.local

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.mefy.platemate.data.local.datastore.DataStoreLanguagePreferenceStore
import com.mefy.platemate.domain.model.language.AppLanguage
import java.io.File
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DataStoreLanguagePreferenceStoreTest {

    @Test
    fun getLanguageOrNull_returnsNull_whenNoPreferenceSaved() = runBlocking {
        val store = createStore()

        assertNull(store.getLanguageOrNull())
    }

    @Test
    fun setLanguage_persistsTrValue() = runBlocking {
        val store = createStore()

        store.setLanguage(AppLanguage.TR)
        assertEquals(AppLanguage.TR, store.getLanguageOrNull())
    }

    @Test
    fun setLanguage_persistsEnValue() = runBlocking {
        val store = createStore()

        store.setLanguage(AppLanguage.EN)
        assertEquals(AppLanguage.EN, store.getLanguageOrNull())
    }

    private fun createStore(): DataStoreLanguagePreferenceStore {
        val dir = File("C:\\tmp\\platemate-tests").apply { mkdirs() }
        val file = File(dir, "language_${UUID.randomUUID()}.preferences_pb")
        file.deleteOnExit()

        val dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
            produceFile = { file }
        )

        return DataStoreLanguagePreferenceStore(dataStore, Unit)
    }
}
