package com.pablo.ducky.data.network

import com.pablo.ducky.data.model.Libro
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/libros/buscar")
    suspend fun buscarLibros(@Query("q") query: String): List<Libro>
}