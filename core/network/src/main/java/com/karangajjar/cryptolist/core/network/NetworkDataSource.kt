package com.karangajjar.cryptolist.core.network

import com.karangajjar.cryptolist.core.model.cryptolistmodel.CryptoListModel
import com.karangajjar.cryptolist.core.model.exchangemodel.ExchangeModel
import com.karangajjar.cryptolist.core.network.retrofit.RetrofitApi
import javax.inject.Inject

interface NetworkDataSource {
    suspend fun getRates() : CryptoListModel
    suspend fun getExchanges(): ExchangeModel
}

class NetworkDataSourceImpl @Inject constructor(
    private val api: RetrofitApi
) : NetworkDataSource {

    override suspend fun getRates(): CryptoListModel {
        return api.getRates()
    }

    override suspend fun getExchanges(): ExchangeModel {
        return api.getExchanges()
    }
}