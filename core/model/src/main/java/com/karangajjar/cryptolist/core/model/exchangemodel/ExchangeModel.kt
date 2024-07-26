package com.karangajjar.cryptolist.core.model.exchangemodel

import com.karangajjar.cryptolist.core.model.exchangemodel.Data

data class ExchangeModel(
    val `data`: List<Data>,
    val timestamp: Long
)