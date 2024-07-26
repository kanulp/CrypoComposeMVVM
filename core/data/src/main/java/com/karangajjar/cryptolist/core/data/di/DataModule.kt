package com.karangajjar.cryptolist.core.data.di

import com.karangajjar.cryptolist.core.data.repository.CryptoListRepository
import com.karangajjar.cryptolist.core.data.repository.CryptoListRepositoryImpl
import com.karangajjar.cryptolist.core.data.repository.ExchangeListRepository
import com.karangajjar.cryptolist.core.data.repository.ExchangeListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsCryptoListRepository(
        cryptoListRepositoryImpl: CryptoListRepositoryImpl,
    ): CryptoListRepository

    @Binds
    internal abstract fun bindsExchangeListRepository(
        exchangeListRepositoryImpl: ExchangeListRepositoryImpl,
    ): ExchangeListRepository

}
