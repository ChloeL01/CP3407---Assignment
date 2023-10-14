package com.example.cp3407_assignment

class ValidatePassword() {
    private val passwordLength = 6

    fun checkPasswordRules(newPasswordString: String): Boolean {
        // Password rules
        val hasLetter = newPasswordString.any { it.isLetter() }
        val hasNumber = newPasswordString.any { it.isDigit() }
        val hasSpecialChar = findSpecialCharacter(newPasswordString)
        val correctLength = newPasswordString.length == passwordLength

        return hasLetter && hasNumber && hasSpecialChar && correctLength
    }

    private fun findSpecialCharacter(newPasswordString: String): Boolean {
        val specialChar = "!$@%"
        for (char in specialChar) {
            if (newPasswordString.contains(char)) {
                return true
            }
        }
        return false
    }

    fun checkNewPasswordsMatch(newPasswordString:String, confirmPasswordString: String): Boolean {
        return newPasswordString == confirmPasswordString
    }
}