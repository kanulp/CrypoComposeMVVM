package com.karangajjar.cryptolist.core.domain.usecase

import com.karangajjar.cryptolist.core.common.Result
import com.karangajjar.cryptolist.core.model.cryptolistmodel.CryptoListModel
import com.karangajjar.cryptolist.core.data.repository.CryptoListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCryptoListUseCase @Inject constructor(
    private val cryptoRepository: CryptoListRepository
) {
    suspend operator fun invoke(): Flow<Result<CryptoListModel>> =
        cryptoRepository.getCryptoList()
}
