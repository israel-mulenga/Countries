package com.example.countries.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.data.CountryApi
import com.example.countries.model.Country
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {
    private val api = CountryApi.create()

    private val _allCountries = MutableStateFlow<List<Country>>(emptyList())

    // État de la barre de recherche
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Liste filtrée qui réagit aux changements de recherche ou de données
    val countries = combine(_allCountries, _searchQuery) { list, query ->
        if (query.isEmpty()) list
        else list.filter { it.name.common.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun fetchCountries(option: String) {
        viewModelScope.launch {
            try {
                // On définit les champs dont on a besoin pour éviter la 404 ou les données trop lourdes
                val fields = "fields=name,flags,capital,population,continents"

                val endpoint = if (option == "all") {
                    "v3.1/all?$fields"
                } else {
                    "v3.1/region/africa?$fields"
                }

                val result = api.getCountries(endpoint)
                _allCountries.value = result

            } catch (e: Exception) {
                android.util.Log.e("API_ERROR", "Erreur: ${e.message}")
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }
}