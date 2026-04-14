package com.example.countries.data

import com.example.countries.model.Country
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface CountryApi {
    @GET
    suspend fun getCountries(@Url url: String): List<Country>

    companion object {
        fun create(): CountryApi {
            // Un client plus patient
            val client = okhttp3.OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://restcountries.com/")
                .client(client) // On dit à Retrofit d'utiliser ce client
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CountryApi::class.java)
        }
    }
}