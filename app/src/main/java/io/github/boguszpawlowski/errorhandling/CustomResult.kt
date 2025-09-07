package io.github.boguszpawlowski.errorhandling

import android.util.Patterns
import io.github.boguszpawlowski.errorhandling.models.PhoneNumber

sealed interface CustomResult<out T, out E> {
    data class Success<T>(val value: T) : CustomResult<T, Nothing>
    data class Failure<E>(val error: E) : CustomResult<Nothing, E>
}

sealed interface PhoneValidationError {
    data object BlankNumber : PhoneValidationError, CommonError.BlankInput
    data object InvalidFormat : PhoneValidationError
}

sealed interface CommonError {
    interface BlankInput : CommonError
}


fun handleCommonErrors(error: CommonError) {
    when (error) {
        is CommonError.BlankInput -> println("Error: Input cannot be blank")
    }
}

fun handleInputWithCustomResult() {
    fun String.toPhoneNumber(): CustomResult<PhoneNumber, PhoneValidationError> {
        return when {
            this.isBlank() -> CustomResult.Failure(PhoneValidationError.BlankNumber)
            !this.matches(regex = Patterns.PHONE.toRegex()) -> CustomResult.Failure(
                PhoneValidationError.InvalidFormat
            )
            else -> CustomResult.Success(PhoneNumber(this))
        }
    }

    val invalidInput = "123132123a"

    // Handling multiple outcomes using custom Result type.
    val phoneNumberResult: CustomResult<PhoneNumber, PhoneValidationError> =
        invalidInput.toPhoneNumber()

    when (phoneNumberResult) {
        is CustomResult.Success -> println("Acquired phone number: $phoneNumberResult")

        is CustomResult.Failure -> {
            when (phoneNumberResult.error) {
                is PhoneValidationError.BlankNumber -> handleCommonErrors(phoneNumberResult.error)
                PhoneValidationError.InvalidFormat -> println("Invalid format for number $invalidInput")
            }
        }
    }
}
