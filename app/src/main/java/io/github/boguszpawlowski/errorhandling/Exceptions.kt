package io.github.boguszpawlowski.errorhandling

import android.util.Patterns
import io.github.boguszpawlowski.errorhandling.models.PhoneNumber

private fun String.toPhoneNumber(): PhoneNumber {
    return if (matches(regex = Patterns.PHONE.toRegex())) {
        return PhoneNumber(this)
    } else {
        throw IllegalArgumentException("Number is invalid")
    }
}

// Requires knowledge of implementation details to handle the exception.
fun handleInputWithExceptions(input: String) {
    try {
        val phoneNumber: PhoneNumber = input.toPhoneNumber()
        println("Acquired phone number: $phoneNumber")
    } catch (e: IllegalArgumentException) {
        println("Failed to acquire phone number: ${e.message}")
    }
}
