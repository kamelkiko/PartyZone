package com.app.partyzone.core.domain.util

open class PartyZoneException(message: String?) : Exception(message)

class NetworkException(override val message: String?) : PartyZoneException(message)

class NotFoundException(override val message: String?) : PartyZoneException(message)

class ValidationNetworkException(override val message: String?) : PartyZoneException(message)

class PermissionDeniedException(override val message: String?) : PartyZoneException(message)

open class AuthorizationException(val errorMessage: String = "") :
    PartyZoneException(errorMessage) {
    data object UnAuthorizedException : AuthorizationException() {
        private fun readResolve(): Any = UnAuthorizedException
    }

    data object InvalidUsernameException : AuthorizationException() {
        private fun readResolve(): Any = InvalidUsernameException
    }

    data object InvalidPasswordException : AuthorizationException() {
        private fun readResolve(): Any = InvalidPasswordException
    }

    data object InvalidEmailException : AuthorizationException() {
        private fun readResolve(): Any = InvalidEmailException
    }

    class UserNotFoundException(message: String) : AuthorizationException(message)

    class InvalidCredentialsException(message: String) : AuthorizationException(message)

    class UserAlreadyExistException(message: String) : AuthorizationException(message)

    class UserBlockedException(message: String) : AuthorizationException(message)
}

class UnknownErrorException(override val message: String?) : PartyZoneException(message)

class InvalidVerificationCodeException(override val message: String?) : PartyZoneException(message)

class ServerErrorException(override val message: String?) : PartyZoneException(message)