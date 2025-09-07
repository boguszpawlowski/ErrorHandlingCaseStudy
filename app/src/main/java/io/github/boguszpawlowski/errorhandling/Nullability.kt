package io.github.boguszpawlowski.errorhandling

import android.util.Patterns
import io.github.boguszpawlowski.errorhandling.models.PhoneNumber

fun String.toPhoneNumberOrNull(): PhoneNumber? {
    return if (matches(regex = Patterns.PHONE.toRegex())) {
        PhoneNumber(this)
    } else {
        null
    }
}

fun handleInputWithNullability(input: String) {
    val phoneNumber: PhoneNumber? = input.toPhoneNumberOrNull()

    if (phoneNumber == null) {
        // This requires knowledge of implementation details to interpret the null case.
        // We are limited to only one possible error message.
        println("Invalid input provided")
    } else {
        println("Acquired phone number: ${phoneNumber.raw}")
    }
}
