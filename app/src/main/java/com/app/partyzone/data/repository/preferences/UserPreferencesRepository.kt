package com.app.partyzone.data.repository.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.app.partyzone.data.util.PreferencesKeys
import com.app.partyzone.data.util.get
import com.app.partyzone.domain.repository.IUserPreferencesRepository
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) :
    IUserPreferencesRepository {
    override suspend fun setUserLoggedIn() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IsLoggedIn] = true
        }
    }

    override suspend fun setUserLoggedOut() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IsLoggedIn] = false
        }
    }

    override suspend fun getUserIsLoggedIn(): Boolean {
        return dataStore.get()[PreferencesKeys.IsLoggedIn] ?: false
    }
}