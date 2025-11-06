package edu.ucne.esthiber_valentin_ap2_p2.presentacion

import edu.ucne.esthiber_valentin_ap2_p2.data.remote.Resource
import edu.ucne.esthiber_valentin_ap2_p2.domain.model.Gasto

data class GastoUiState(
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val listaGastos: Resource<List<Gasto>> = Resource.Loading(),

    val showBottomSheet: Boolean = false,

    val gastoId: Int = 0,
    val gastoFecha: String = "",
    val gastoSuplidor: String = "",
    val gastoNcf: String = "",
    val gastoItbis: String = "",
    val gastoMonto: String = "",
)
