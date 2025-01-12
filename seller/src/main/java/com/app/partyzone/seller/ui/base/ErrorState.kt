package com.app.partyzone.seller.ui.base

import com.app.partyzone.core.AuthorizationException

sealed interface ErrorState {
    data class NetworkError(val message: String?) : ErrorState
    data class NotFound(val message: String?) : ErrorState
    data class UnknownError(val message: String?) : ErrorState
    data class InvalidVerificationCode(val message: String?) : ErrorState
    data class ValidationNetwork(val message: String?) : ErrorState
    data class ServerError(val message: String?) : ErrorState
    data class PermissionDenied(val message: String?) : ErrorState

    // region Authorization
    data object UnAuthorized : ErrorState
    data object InvalidUsername : ErrorState
    data object InvalidPassword : ErrorState
    data object InvalidEmail : ErrorState
    data class UserAlreadyExists(val message: String) : ErrorState
    data class UserNotFound(val message: String) : ErrorState
    data class UserBlocked(val message: String) : ErrorState
    data class WrongPassword(val message: String) : ErrorState
    // endregion
}

fun handelAuthorizationException(
    exception: AuthorizationException,
    onError: (t: ErrorState) -> Unit,
) {
    when (exception) {
        is AuthorizationException.UnAuthorizedException -> onError(ErrorState.UnAuthorized)
        is AuthorizationException.InvalidUsernameException -> onError(ErrorState.InvalidUsername)
        is AuthorizationException.InvalidPasswordException -> onError(ErrorState.InvalidPassword)
        is AuthorizationException.InvalidCredentialsException -> onError(
            ErrorState.WrongPassword(exception.errorMessage)
        )

        is AuthorizationException.UserAlreadyExistException -> onError(
            ErrorState.UserAlreadyExists(exception.errorMessage)
        )

        is AuthorizationException.UserNotFoundException -> onError(ErrorState.UserNotFound(exception.errorMessage))
        is AuthorizationException.UserBlockedException -> onError(ErrorState.UserBlocked(exception.errorMessage))
        AuthorizationException.InvalidEmailException -> onError(ErrorState.InvalidEmail)
    }
}