package com.example.cp3407_assignment
import org.junit.Test

import org.junit.Assert.*


class ValidatePasswordTest {
    private val validatePassword = ValidatePassword()

    @Test
    fun passwordRules_isCorrect(){
        assertEquals(true, validatePassword.checkPasswordRules("password1!"))
    }

    @Test
    fun passwordRules_isIncorrect(){
        assertNotEquals(true, validatePassword.checkPasswordRules("password"))
    }

    @Test
    fun passwordMatch_isCorrect(){
        assertEquals(true, validatePassword.checkNewPasswordsMatch("password1!", "password1!"))
    }

    @Test
    fun passwordMatch_isIncorrect(){
        assertNotEquals(true, validatePassword.checkNewPasswordsMatch("password1!", "password2!"))
    }
}