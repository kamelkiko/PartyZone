package com.app.partyzone.seller.ui.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partyzone.core.domain.repository.IUserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: IUserPreferencesRepository,
) : ViewModel() {

    private val _effect = MutableSharedFlow<OnboardingEffect>()
    val effect = _effect.asSharedFlow()

    fun setFirstTimeOpenApp() {
        viewModelScope.launch {
            userPreferencesRepository.setSellerFirstTimeUseApp()
            _effect.emit(OnboardingEffect.NavigateToLogin)
        }
    }
}