package com.karangajjar.cryptolist.feature.exchangelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karangajjar.cryptolist.core.domain.usecase.GetExchangeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.karangajjar.cryptolist.core.common.Result
import com.karangajjar.cryptolist.core.model.exchangemodel.Data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExchangeListViewModel @Inject constructor(
    private val exchangeListUseCase: GetExchangeListUseCase
) : ViewModel()
{
    private val _exchangeListState = MutableStateFlow<ExchangeListUiState>(ExchangeListUiState.Loading)
    val exchangeListState get() = _exchangeListState.asStateFlow()

    init {
        fetchExchangeList()
    }

    private fun fetchExchangeList() {
        viewModelScope.launch {
            exchangeListUseCase.invoke().collect { response ->
                when(response){
                    is Result.Error -> _exchangeListState.value  = ExchangeListUiState.Error("Something went wrong")
                    is Result.Loading -> _exchangeListState.value = ExchangeListUiState.Loading
                    is Result.Success -> if (response.data.data.isEmpty()){
                        _exchangeListState.value = ExchangeListUiState.ExchangeListEmpty
                        return@collect
                    }
                    else {
                        _exchangeListState.value = ExchangeListUiState.Success(response.data.data)
                    }

                }
            }
        }
    }

}



sealed interface ExchangeListUiState {
    data class Success(val exchangeList : List<Data>) : ExchangeListUiState
    data class Error(val message:String) : ExchangeListUiState
    data object Loading : ExchangeListUiState
    data object ExchangeListEmpty:ExchangeListUiState
}