package com.karangajjar.cryptolist.core.data.repository

import com.karangajjar.cryptolist.core.common.Result
import com.karangajjar.cryptolist.core.model.cryptolistmodel.CryptoListModel
import com.karangajjar.cryptolist.core.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class CryptoListRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : CryptoListRepository {


    override suspend fun getCryptoList(): Flow<Result<CryptoListModel>>{
        val cryptoList = networkDataSource.getRates()
        return flowOf(Result.Success(cryptoList))
    }
}