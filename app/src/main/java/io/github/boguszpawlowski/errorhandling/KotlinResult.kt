package io.github.boguszpawlowski.errorhandling

import android.util.Patterns
import io.github.boguszpawlowski.errorhandling.models.PhoneNumber

private fun String.toPhoneNumber(): Result<PhoneNumber> {
    return if (isBlank()) {
        Result.failure(IllegalArgumentException("Number cannot be blank"))
    } else if (matches(regex = Patterns.PHONE.toRegex())) {
        Result.failure(IllegalArgumentException("Invalid phone number"))
    } else {
        Result.success(PhoneNumber(this))
    }
}

fun handleInputWithKotlinResult(input: String) {

    val phoneNumberResult: Result<PhoneNumber> = input.toPhoneNumber()

    phoneNumberResult.onSuccess { phoneNumber ->
        println("Acquired phone number: ${phoneNumber.raw}")
    }.onFailure { exception ->
        when (exception) {
            is IllegalArgumentException -> {
                // This requires knowledge of implementation details to interpret the exception.
                when (exception.message) {
                    "Number cannot be blank" -> println("Number cannot be blank")
                    "Invalid phone number" -> println("$input is invalid")
                    else -> println("An unexpected error occurred: ${exception.message}")
                }
            }

            // non-exhaustive when!!
            else -> {
                println("An unexpected error occurred: ${exception.message}")
            }
        }
    }
}
