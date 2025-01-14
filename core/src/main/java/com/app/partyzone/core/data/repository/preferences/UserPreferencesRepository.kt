package com.app.partyzone.core.data.repository.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.app.partyzone.core.data.util.PreferencesKeys
import com.app.partyzone.core.data.util.get
import com.app.partyzone.core.domain.repository.IUserPreferencesRepository
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) :
    IUserPreferencesRepository {
    override suspend fun setUserFirstTimeUseApp() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.isFirstTimeOpenApp] = true
        }
    }

    override suspend fun getUserIsFirstTimeOpenApp(): Boolean {
        return dataStore.get()[PreferencesKeys.isFirstTimeOpenApp] ?: false
    }
}