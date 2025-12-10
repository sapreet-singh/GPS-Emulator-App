package com.example.gpsemulator.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onPlaceSelected: (String) -> Unit // returns Place ID (or LatLng if we fetch it here)
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()

    Scaffold(
        topBar = {
            SearchBar(
                query = query,
                onQueryChange = viewModel::onQueryChanged,
                onSearch = {},
                active = true,
                onActiveChange = {},
                placeholder = { Text("Search location") },
                leadingIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                }
            ) {
                LazyColumn {
                    items(results) { result ->
                        ListItem(
                            headlineContent = { Text(result.primaryText) },
                            supportingContent = { Text(result.secondaryText) },
                            modifier = Modifier.clickable { onPlaceSelected(result.placeId) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        // SearchBar handles content
        Column(modifier = Modifier.padding(padding)) {}
    }
}
