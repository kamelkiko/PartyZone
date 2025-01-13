package com.app.partyzone.seller.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partyzone.core.domain.util.AuthorizationException
import com.app.partyzone.core.domain.util.InvalidVerificationCodeException
import com.app.partyzone.core.domain.util.NetworkException
import com.app.partyzone.core.domain.util.NotFoundException
import com.app.partyzone.core.domain.util.PermissionDeniedException
import com.app.partyzone.core.domain.util.ServerErrorException
import com.app.partyzone.core.domain.util.UnknownErrorException
import com.app.partyzone.core.domain.util.ValidationNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

abstract class BaseViewModel<S, E>(initialState: S) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E?>()
    val effect: SharedFlow<E?> = _effect.asSharedFlow()

    protected fun <T> tryToExecute(
        function: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (ErrorState) -> Unit,
    ): Job {
        return runWithErrorCheck(onError) {
            val result = function()
            onSuccess(result)
        }
    }

    protected suspend fun <T> tryToExecuteWithoutScope(
        function: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (ErrorState) -> Unit,
    ) {
        try {
            val result = function()
            onSuccess(result)
        } catch (exception: NetworkException) {
            onError(ErrorState.NetworkError(exception.message))
        } catch (exception: AuthorizationException) {
            handelAuthorizationException(exception, onError)
        } catch (exception: ServerErrorException) {
            onError(ErrorState.ServerError(exception.message))
        } catch (exception: InvalidVerificationCodeException) {
            onError(ErrorState.InvalidVerificationCode(exception.message))
        } catch (exception: ValidationNetworkException) {
            onError(ErrorState.ValidationNetwork(exception.message))
        } catch (exception: NotFoundException) {
            onError(ErrorState.NotFound(exception.message))
        } catch (exception: PermissionDeniedException) {
            onError(ErrorState.PermissionDenied(exception.message))
        } catch (exception: UnknownErrorException) {
            onError(ErrorState.UnknownError(exception.message))
        } catch (exception: Exception) {
            onError(ErrorState.UnknownError(exception.message))
        }
    }

    protected fun <T> tryToCollect(
        function: suspend () -> Flow<T>,
        onNewValue: (T) -> Unit,
        onError: (ErrorState) -> Unit,
    ): Job {
        return runWithErrorCheck(onError) {
            function().distinctUntilChanged().collectLatest {
                onNewValue(it)
            }
        }
    }

    protected fun updateState(updater: (S) -> S) {
        _state.update(updater)
    }

    protected fun sendNewEffect(newEffect: E) {
        viewModelScope.launch(Dispatchers.IO) {
            _effect.emit(newEffect)
        }
    }

    private fun runWithErrorCheck(
        onError: (ErrorState) -> Unit,
        inScope: CoroutineScope = viewModelScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        function: suspend () -> Unit,
    ): Job {
        return inScope.launch(dispatcher) {
            try {
                function()
            } catch (exception: NetworkException) {
                onError(ErrorState.NetworkError(exception.message))
            } catch (exception: AuthorizationException) {
                handelAuthorizationException(exception, onError)
            } catch (exception: ServerErrorException) {
                onError(ErrorState.ServerError(exception.message))
            } catch (exception: NotFoundException) {
                onError(ErrorState.NotFound(exception.message))
            } catch (exception: ValidationNetworkException) {
                onError(ErrorState.ValidationNetwork(exception.message))
            } catch (exception: PermissionDeniedException) {
                onError(ErrorState.PermissionDenied(exception.message))
            } catch (exception: InvalidVerificationCodeException) {
                onError(ErrorState.InvalidVerificationCode(exception.message))
            } catch (exception: UnknownErrorException) {
                onError(ErrorState.UnknownError(exception.message))
            } catch (exception: Exception) {
                onError(ErrorState.UnknownError(exception.message))
            }
        }
    }

    private fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> {
        require(periodMillis > 0)
        return flow {
            var lastTime = 0L
            collect { value ->
                val currentTime = Clock.System.now().toEpochMilliseconds()
                if (currentTime - lastTime >= periodMillis) {
                    lastTime = currentTime
                    emit(value)
                }
            }
        }
    }

    protected fun launchDelayed(duration: Long, block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            delay(duration)
            block()
        }
    }
}