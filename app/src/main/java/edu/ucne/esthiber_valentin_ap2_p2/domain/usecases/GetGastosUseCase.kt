package edu.ucne.esthiber_valentin_ap2_p2.domain.usecases

import edu.ucne.esthiber_valentin_ap2_p2.data.remote.Resource
import edu.ucne.esthiber_valentin_ap2_p2.domain.model.Gasto
import edu.ucne.esthiber_valentin_ap2_p2.domain.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGastosUseCase @Inject constructor(
    private val repository: GastoRepository
) {
    operator fun invoke(): Flow<Resource<List<Gasto>>> = repository.getGastos()
}