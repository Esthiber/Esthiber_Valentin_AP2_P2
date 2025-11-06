package edu.ucne.esthiber_valentin_ap2_p2.domain.usecases

import edu.ucne.esthiber_valentin_ap2_p2.data.dto.GastoRequest
import edu.ucne.esthiber_valentin_ap2_p2.domain.repository.GastoRepository
import javax.inject.Inject

class UpdateGastoUseCase@Inject constructor(
    private val repository: GastoRepository
) {
    suspend operator fun invoke(id: Int, req: GastoRequest) =
        repository.putGasto(id, req)
}