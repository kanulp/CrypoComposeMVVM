package com.karangajjar.cryptolist.core.data.repository

import com.karangajjar.cryptolist.core.common.Result
import com.karangajjar.cryptolist.core.model.exchangemodel.ExchangeModel
import kotlinx.coroutines.flow.Flow

interface ExchangeListRepository {

    suspend fun getExchanges(): Flow<Result<ExchangeModel>>

}