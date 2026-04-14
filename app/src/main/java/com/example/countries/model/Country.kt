package com.example.countries.model

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("name")
    val name: CountryName,
    @SerializedName("flags")
    val flags: Flag,
    @SerializedName("capital")
    val capital: List<String>? = emptyList(), // Optionnel car certains pays n'ont pas de capitale
    @SerializedName("population")
    val population: Long = 0,
    @SerializedName("continents")
    val continents: List<String>? = emptyList()
)

data class CountryName(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Flag(
    @SerializedName("png")
    val png: String,
    @SerializedName("alt")
    val alt: String? = ""
)