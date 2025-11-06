package edu.ucne.esthiber_valentin_ap2_p2.domain.repository

import edu.ucne.esthiber_valentin_ap2_p2.data.dto.GastoRequest
import edu.ucne.esthiber_valentin_ap2_p2.data.remote.Resource
import edu.ucne.esthiber_valentin_ap2_p2.domain.model.Gasto
import kotlinx.coroutines.flow.Flow

interface GastoRepository {
    fun getGastos(): Flow<Resource<List<Gasto>>>
    fun getGastoById(id: Int): Flow<Resource<Gasto>>
    suspend fun postGasto(req: GastoRequest): Resource<Unit>
    suspend fun putGasto(id: Int, req: GastoRequest): Resource<Unit>
}