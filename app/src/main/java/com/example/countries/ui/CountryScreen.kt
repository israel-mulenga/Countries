package com.example.countries.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.countries.R
import com.example.countries.model.Country
import com.example.countries.viewmodel.CountryViewModel

@Composable
fun MainNavigation(viewModel: CountryViewModel = viewModel()) {
    // Gestion simple de la navigation par état
    var currentScreen by remember { mutableStateOf("home") }

    if (currentScreen == "home") {
        HomeScreen(onOptionSelected = { endpoint ->
            viewModel.fetchCountries(endpoint)
            currentScreen = "list"
        })
    } else {
        CountryListScreen(viewModel, onBack = { currentScreen = "home" })
    }
}

@Composable
fun HomeScreen(onOptionSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenue sur CountryApp", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onOptionSelected("all") },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Tous les pays du monde")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onOptionSelected("region/africa") },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Pays d'Afrique")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryListScreen(viewModel: CountryViewModel, onBack: () -> Unit) {
    val countries by viewModel.countries.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Liste des Pays") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Barre de recherche
            SearchBar(query = searchQuery, onQueryChanged = { viewModel.onSearchQueryChanged(it) })

            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(countries) { country ->
                    CountryItem(country)
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        placeholder = { Text("Rechercher un pays...") },
        singleLine = true,
        leadingIcon = { Icon(painterResource(android.R.drawable.ic_menu_search), contentDescription = null) }
    )
}

@Composable
fun CountryItem(country: Country) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(country.flags.png).crossfade(true).build(),
                contentDescription = null,
                error = painterResource(R.drawable.error),
                placeholder = painterResource(R.drawable.load),
                modifier = Modifier.size(width = 100.dp, height = 60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = country.name.common, style = MaterialTheme.typography.titleMedium)
                Text(text = "Capitale: ${country.capital?.joinToString() ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Population: ${String.format("%,d", country.population)}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}