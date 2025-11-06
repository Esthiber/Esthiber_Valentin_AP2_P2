package edu.ucne.esthiber_valentin_ap2_p2.data.dto

data class GastoResponse(
    val gastoId: Int?,
    val fecha: String,
    val suplidor: String,
    val ncf: String,
    val itbis: Double,
    val monto: Double,
)

data class GastoRequest(
    val fecha: String,
    val suplidor: String,
    val ncf: String,
    val itbis: Double,
    val monto: Double,
)

