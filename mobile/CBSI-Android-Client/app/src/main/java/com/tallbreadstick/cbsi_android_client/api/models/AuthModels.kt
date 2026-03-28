package com.tallbreadstick.cbsi_android_client.api.models

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String?, val mustChangePassword: Boolean?)

data class RegisterRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val sex: String,
    val dateOfBirth: String,
    val permanentAddress: String?,
    val currentAddress: String?
)

data class RegisterResponse(val message: String?, val generatedPassword: String?, val schoolId: String?)

data class ChangePasswordRequest(val email: String, val oldPassword: String, val newPassword: String)

data class GenericResponse(val message: String?)
