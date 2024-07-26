package com.karangajjar.cryptolist.core.domain.usecase

import com.karangajjar.cryptolist.core.common.Result
import com.karangajjar.cryptolist.core.model.exchangemodel.ExchangeModel
import com.karangajjar.cryptolist.core.data.repository.ExchangeListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeListUseCase @Inject constructor(
    private val exchangeListRepository: ExchangeListRepository
) {
    suspend operator fun invoke(): Flow<Result<ExchangeModel>> =
        exchangeListRepository.getExchanges()
}
