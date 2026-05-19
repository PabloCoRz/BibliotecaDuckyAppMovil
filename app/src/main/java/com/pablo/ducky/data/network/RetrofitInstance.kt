package com.pablo.ducky.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton de Retrofit para consumir la API de la biblioteca.
 *
 * BASE_URL apunta a localhost del host (útil para el emulador de Android).
 * Si el servidor no responde, el repositorio hace fallback a Room automáticamente.
 */
object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
