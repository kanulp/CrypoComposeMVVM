package com.karangajjar.cryptolist.core.network.retrofit

import com.karangajjar.cryptolist.core.model.cryptolistmodel.CryptoListModel
import com.karangajjar.cryptolist.core.model.exchangemodel.ExchangeModel
import retrofit2.http.GET

interface RetrofitApi {

    @GET("rates")
    suspend fun getRates(): CryptoListModel

    @GET("exchanges")
    suspend fun getExchanges(): ExchangeModel
}