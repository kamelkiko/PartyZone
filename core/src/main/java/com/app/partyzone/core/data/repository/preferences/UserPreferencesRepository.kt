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
            preferences[PreferencesKeys.isFirstTimeOpenApp] = false
        }
    }

    override suspend fun getUserIsFirstTimeOpenApp(): Boolean {
        return dataStore.get()[PreferencesKeys.isFirstTimeOpenApp] ?: true
    }

    override suspend fun setSellerFirstTimeUseApp() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.isFirstTimeSellerOpenApp] = false
        }
    }

    override suspend fun getSellerIsFirstTimeOpenApp(): Boolean {
        return dataStore.get()[PreferencesKeys.isFirstTimeSellerOpenApp] ?: true
    }
}