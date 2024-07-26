package com.karangajjar.cryptolist.core.data.repository

import com.karangajjar.cryptolist.core.common.Result
import com.karangajjar.cryptolist.core.model.exchangemodel.ExchangeModel
import com.karangajjar.cryptolist.core.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class ExchangeListRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : ExchangeListRepository {


    override suspend fun getExchanges(): Flow<Result<ExchangeModel>> {
        val exchangeList = networkDataSource.getExchanges()
        return flowOf(Result.Success(exchangeList))
    }
}