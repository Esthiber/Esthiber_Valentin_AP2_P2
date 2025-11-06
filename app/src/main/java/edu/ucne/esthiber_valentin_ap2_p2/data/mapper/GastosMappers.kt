package edu.ucne.esthiber_valentin_ap2_p2.data.mapper

import edu.ucne.esthiber_valentin_ap2_p2.data.dto.GastoResponse
import edu.ucne.esthiber_valentin_ap2_p2.domain.model.Gasto

fun GastoResponse.toDomain() = Gasto(
    gastoId = gastoId ?: 0,
    fecha = fecha,
    suplidor = suplidor,
    ncf = ncf,
    itbis = itbis,
    monto = monto,
)

