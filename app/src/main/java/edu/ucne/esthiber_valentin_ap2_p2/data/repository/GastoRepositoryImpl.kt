package edu.ucne.esthiber_valentin_ap2_p2.data.repository

import edu.ucne.esthiber_valentin_ap2_p2.data.dto.GastoRequest
import edu.ucne.esthiber_valentin_ap2_p2.data.mapper.toDomain
import edu.ucne.esthiber_valentin_ap2_p2.data.remote.RemoteDataSource
import edu.ucne.esthiber_valentin_ap2_p2.data.remote.Resource
import edu.ucne.esthiber_valentin_ap2_p2.domain.model.Gasto
import edu.ucne.esthiber_valentin_ap2_p2.domain.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GastoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): GastoRepository {
    override fun getGastos(): Flow<Resource<List<Gasto>>> = flow {
        try {
            emit(Resource.Loading())
            val gastosDto = remoteDataSource.getGastos()
            val gastos = gastosDto.map { dto -> dto.toDomain() }
            emit(Resource.Success(gastos))
        } catch (e: Exception) {
            emit(Resource.Error("Ocurrio un error inesperado: ${e.message}"))
        }
    }

    override fun getGastoById(id: Int): Flow<Resource<Gasto>> = flow {
        try {
            emit(Resource.Loading())
            val gastoDto = remoteDataSource.getGastoById(id)
            val gasto = gastoDto.toDomain()
            emit(Resource.Success(gasto))
        } catch (e: Exception) {
            emit(Resource.Error("Ocurrio un error inesperado: ${e.message}"))
        }
    }

    override suspend fun postGasto(req: GastoRequest): Resource<Unit> {
        return try{
            remoteDataSource.postGasto(req)
            Resource.Success(Unit)
        }catch (e:Exception){
            Resource.Error("Ocurrio un error inesperado")
        }
    }

    override suspend fun putGasto(id: Int, req: GastoRequest): Resource<Unit> {
        return try{
            remoteDataSource.putGasto(id, req)
            Resource.Success(Unit)
        }catch( e:Exception){
            Resource.Error("Ocurrio un error inesperado")
        }
    }
}