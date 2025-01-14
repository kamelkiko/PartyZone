package com.app.partyzone.core.data.util

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val isFirstTimeOpenApp = booleanPreferencesKey("is_first_time_open_app")
}