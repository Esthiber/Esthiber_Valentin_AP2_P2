package edu.ucne.esthiber_valentin_ap2_p2.data.remote

import edu.ucne.esthiber_valentin_ap2_p2.data.dto.GastoRequest
import edu.ucne.esthiber_valentin_ap2_p2.data.dto.GastoResponse
import edu.ucne.esthiber_valentin_ap2_p2.domain.model.Gasto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: GastoApi
) {
    suspend fun postGasto(req: GastoRequest) =
        api.postGasto(req)
    suspend fun getGastos(): List<GastoResponse> =
        api.getGastos()

    // TODO Implementar correctamente Resource<GastoResponse> podria ser
    suspend fun getGastoById(id: Int): GastoResponse =
        api.getGastoById(id)

    suspend fun putGasto(id: Int, req: GastoRequest): Resource<Unit> {
        return try {
            api.putGasto(id, req)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error de red: ${e.localizedMessage ?: "Ocurrio un error desconocido"}")
        }
    }
}