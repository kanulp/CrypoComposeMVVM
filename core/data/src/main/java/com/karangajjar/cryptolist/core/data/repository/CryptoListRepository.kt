package com.karangajjar.cryptolist.core.data.repository

import com.karangajjar.cryptolist.core.common.Result
import com.karangajjar.cryptolist.core.model.cryptolistmodel.CryptoListModel
import kotlinx.coroutines.flow.Flow

interface CryptoListRepository {

    suspend fun getCryptoList(): Flow<Result<CryptoListModel>>

}