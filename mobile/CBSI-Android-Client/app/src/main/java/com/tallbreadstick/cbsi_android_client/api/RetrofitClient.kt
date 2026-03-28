package com.tallbreadstick.cbsi_android_client.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Use 10.0.2.2 for emulator to reach host's localhost:8080
    private const val BASE_URL = "http://10.0.2.2:8080"

    lateinit var api: ApiService

    fun init(context: Context) {
        val prefs = context.getSharedPreferences("cbsi_prefs", Context.MODE_PRIVATE)

        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val req = chain.request().newBuilder()
                prefs.getString("cbsi_token", null)?.let { token ->
                    req.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(req.build())
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)
    }
}
