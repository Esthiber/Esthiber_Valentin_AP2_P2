package edu.ucne.esthiber_valentin_ap2_p2.data.remote

import edu.ucne.esthiber_valentin_ap2_p2.data.dto.GastoRequest
import edu.ucne.esthiber_valentin_ap2_p2.data.dto.GastoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GastoApi {
    @POST("api/Gastos")
    suspend fun postGasto(@Body gasto: GastoRequest): GastoResponse

    @GET("api/Gastos")
    suspend fun getGastos(): List<GastoResponse>

    @GET("api/Gastos/{id}")
    suspend fun getGastoById(@Path("id") id: Int): GastoResponse

    @PUT("api/Gastos/{id}")
    suspend fun putGasto(@Path("id") id:Int, @Body gasto: GastoRequest): GastoResponse

}