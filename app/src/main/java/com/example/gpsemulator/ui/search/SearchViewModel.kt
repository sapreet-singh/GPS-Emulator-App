package com.example.gpsemulator.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchResult(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val placesClient: PlacesClient
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _results = MutableStateFlow<List<SearchResult>>(emptyList())
    val results = _results.asStateFlow()

    private var searchJob: Job? = null
    private val token = AutocompleteSessionToken.newInstance()

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        searchJob?.cancel()
        
        if (newQuery.isBlank()) {
            _results.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            searchPlaces(newQuery)
        }
    }

    private fun searchPlaces(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                _results.value = response.autocompletePredictions.map { 
                    SearchResult(it.placeId, it.primaryText(null).toString(), it.secondaryText(null).toString())
                }
            }
            .addOnFailureListener {
                _results.value = emptyList()
                it.printStackTrace()
            }
    }
}
