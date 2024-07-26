package com.karangajjar.cryptolist.ratelist

//import com.karangajjar.cryptolist.core.common.Result
//import com.karangajjar.cryptolist.core.domain.usecase.GetCryptoListUseCase
//import com.karangajjar.cryptolist.core.model.cryptolistmodel.CryptoListModel
//import com.karangajjar.cryptolist.core.model.cryptolistmodel.Data
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.test.*
//import org.junit.After
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertTrue
//import org.junit.Before
//import org.junit.Test
//
//@ExperimentalCoroutinesApi
//class RateListViewModelTest {
//
//    private val testDispatcher = UnconfinedTestDispatcher()
//    private lateinit var viewModel: RateListViewModel
//    private lateinit var getCryptoListUseCase: GetCryptoListUseCase
//
//    @Before
//    fun setUp() {
//        Dispatchers.setMain(testDispatcher)
//        getCryptoListUseCase = mockk()
//        viewModel = RateListViewModel(getCryptoListUseCase)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun fetchCryptoList_success() = runTest {
//        // Given
//        val data = listOf(
//            Data(id = "bitcoin", rateUsd = "45000", currencySymbol = "BTC", symbol = "BTC", type = "crypto"),
//            Data(id = "ethereum", rateUsd = "3000", currencySymbol = "BTC", symbol = "BTC", type = "crypto"),
//        )
//        val cryptoListModel = CryptoListModel(data, timestamp = 1L)
//        val flow = flow {
//            emit(Result.Loading)
//            emit(Result.Success(cryptoListModel))
//        }
//        coEvery { getCryptoListUseCase.invoke() } returns flow
//
//        // When
//        viewModel.fetchCryptoList()
//
//        // Then
//        assertTrue(viewModel.cryptoListState.value is RateListUiState.Success)
//        assertEquals(data, (viewModel.cryptoListState.value as RateListUiState.Success).rateList)
//    }
//
//    @Test
//    fun fetchCryptoList_empty() = runTest {
//        // Given
//        val flow = flow {
//            emit(Result.Loading)
//            emit(Result.Success(CryptoListModel(emptyList(), timestamp = 0L)))
//        }
//        coEvery { getCryptoListUseCase.invoke() } returns flow
//
//        // When
//        viewModel.fetchCryptoList()
//
//        // Then
//        assertTrue(viewModel.cryptoListState.value is RateListUiState.RateListEmpty)
//    }
//
//    @Test
//    fun fetchCryptoList_error() = runTest {
//        // Given
//        val flow = flow {
//            emit(Result.Loading)
//            emit(Result.Error(Exception("Test exception")))
//        }
//        coEvery { getCryptoListUseCase.invoke() } returns flow
//
//        // When
//        viewModel.fetchCryptoList()
//
//        // Then
//        assertTrue(viewModel.cryptoListState.value is RateListUiState.Error)
//        assertEquals("Something went wrong", (viewModel.cryptoListState.value as RateListUiState.Error).message)
//    }
//}

import com.karangajjar.cryptolist.core.data.repository.CryptoListRepository
import com.karangajjar.cryptolist.core.domain.usecase.GetCryptoListUseCase
import com.karangajjar.cryptolist.core.model.cryptolistmodel.CryptoListModel
import com.karangajjar.cryptolist.core.model.cryptolistmodel.Data
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import com.karangajjar.cryptolist.core.common.Result
import org.junit.Test

@ExperimentalCoroutinesApi
class RateListViewModelTest {

//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var cryptoListRepository: CryptoListRepository

    private lateinit var getCryptoListUseCase: GetCryptoListUseCase
    private lateinit var viewModel: RateListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        getCryptoListUseCase = GetCryptoListUseCase(cryptoListRepository)
        viewModel = RateListViewModel(getCryptoListUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCryptoList success`() = runTest {
        // Given
        val cryptoListData = listOf(
            Data(id = "bitcoin", rateUsd = "45000", currencySymbol = "BTC", symbol = "BTC", type = "crypto"),
            Data(id = "ethereum", rateUsd = "3000", currencySymbol = "BTC", symbol = "BTC", type = "crypto"),
        )

        val cryptoListModel = CryptoListModel(data = cryptoListData, timestamp = 1L)

        val flow = flow {
            emit(Result.Loading)
            emit(Result.Success(cryptoListModel))
        }
        coEvery { getCryptoListUseCase.invoke() } returns flow

        // When
        viewModel.fetchCryptoList()

        // Then
        assertEquals(RateListUiState.Success(cryptoListData), viewModel.cryptoListState.value)
    }

    @Test
    fun `fetchCryptoList error`() = runTest {
        // Given
        val errorMessage = "Something went wrong"
        coEvery { cryptoListRepository.getCryptoList() } returns flowOf(Result.Error(Exception(errorMessage)))

        // When
        viewModel.fetchCryptoList()

        // Then
        assertEquals(RateListUiState.Error(errorMessage), viewModel.cryptoListState.value)
    }

    @Test
    fun `fetchCryptoList empty`() = runTest {
        // Given
        val emptyCryptoListModel = CryptoListModel(data = emptyList(), timestamp = 0L)
        coEvery { cryptoListRepository.getCryptoList() } returns flowOf(Result.Success(emptyCryptoListModel))

        // When
        viewModel.fetchCryptoList()

        // Then
        assertEquals(RateListUiState.RateListEmpty, viewModel.cryptoListState.value)
    }

    @Test
    fun `toggleSortOrder changes sort order`() = runTest {
        // Given
        val initialSortOrder = viewModel.isSortedDescending.value

        // When
        viewModel.toggleSortOrder()

        // Then
        assertEquals(!initialSortOrder, viewModel.isSortedDescending.value)
    }

    @Test
    fun `updateSearchQuery updates search query`() = runTest {
        // Given
        val newQuery = "bitcoin"

        // When
        viewModel.updateSearchQuery(newQuery)

        // Then
        assertEquals(newQuery, viewModel.searchQuery.value)
    }

    @Test
    fun `filteredRateList filters and sorts correctly`() = runTest {
        // Given
        val cryptoListData = listOf(
            Data(id = "bitcoin", rateUsd = "45000", currencySymbol = "BTC", symbol = "BTC", type = "crypto"),
            Data(id = "ethereum", rateUsd = "3000", currencySymbol = "BTC", symbol = "BTC", type = "crypto"),
        )
        //viewModel.cryptoListState.value = RateListUiState.Success(cryptoListData)

        // When
        viewModel.updateSearchQuery("bit") // Filter by "bit"
        viewModel.toggleSortOrder() // Sort ascending

        // Then
        val expectedFilteredList = listOf(
            Data(id = "bitcoin", rateUsd = "45000", currencySymbol = "BTC", symbol = "BTC", type = "crypto"),
            Data(id = "ethereum", rateUsd = "3000", currencySymbol = "BTC", symbol = "BTC", type = "crypto"),
        )
        assertEquals(
            RateListUiState.Success(expectedFilteredList),
            viewModel.filteredRateList.value
        )
    }
}