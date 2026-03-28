package com.tallbreadstick.cbsi_android_client.api

import com.tallbreadstick.cbsi_android_client.api.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/login")
    fun login(@Body req: LoginRequest): Call<LoginResponse>

    @POST("/api/auth/register")
    fun register(@Body req: RegisterRequest): Call<RegisterResponse>

    @POST("/api/auth/change-password")
    fun changePassword(@Body req: ChangePasswordRequest): Call<GenericResponse>
}
