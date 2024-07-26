package com.karangajjar.cryptolist.ratelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karangajjar.cryptolist.core.domain.usecase.GetCryptoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.karangajjar.cryptolist.core.common.Result
import com.karangajjar.cryptolist.core.model.cryptolistmodel.Data
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class RateListViewModel  @Inject constructor(
    private val rateListUseCase: GetCryptoListUseCase
) : ViewModel(){

    private val _cryptoListState = MutableStateFlow<RateListUiState>(RateListUiState.Loading)
    val cryptoListState get() = _cryptoListState.asStateFlow()


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isSortedDescending = MutableStateFlow(false)
    val isSortedDescending: StateFlow<Boolean> = _isSortedDescending

    init {
        fetchCryptoList()
    }

    fun toggleSortOrder() {
        _isSortedDescending.value = !_isSortedDescending.value
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    val filteredRateList: StateFlow<RateListUiState> = combine(_cryptoListState, _searchQuery, _isSortedDescending) { state, query, isDescending ->
        if (state is RateListUiState.Success) {
            val filteredData = state.rateList
                .filter { it.id.contains(query, ignoreCase = true) }
                .sortedByDescending { if (isDescending) it.rateUsd.toDouble() else -it.rateUsd.toDouble() }
            RateListUiState.Success(filteredData)
        } else {
            state
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, RateListUiState.Loading)

    fun fetchCryptoList() {
        viewModelScope.launch {
            rateListUseCase.invoke().collect { response ->
                when(response){
                    is Result.Error -> _cryptoListState.value  = RateListUiState.Error("Something went wrong")
                    is Result.Loading -> _cryptoListState.value = RateListUiState.Loading
                    is Result.Success -> if (response.data.data.isEmpty()){
                        _cryptoListState.value = RateListUiState.RateListEmpty
                        return@collect
                    }
                    else {
                        _cryptoListState.value = RateListUiState.Success(response.data.data)
                    }

                }
                Log.d("CryptoListViewModel", "fetchCryptoList response : $response ")
            }
        }
    }
}

sealed interface RateListUiState {
    data class Success(val rateList : List<Data>) : RateListUiState
    data class Error(val message:String) : RateListUiState
    data object Loading : RateListUiState
    data object RateListEmpty:RateListUiState
}