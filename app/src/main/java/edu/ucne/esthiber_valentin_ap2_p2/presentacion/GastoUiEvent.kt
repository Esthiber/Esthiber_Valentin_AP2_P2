package edu.ucne.esthiber_valentin_ap2_p2.presentacion

import edu.ucne.esthiber_valentin_ap2_p2.domain.model.Gasto

interface GastoUiEvent {
    data object Load : GastoUiEvent

    data class  getGasto(val id: Int): GastoUiEvent
    
    data class CrearGasto(val gasto: Gasto) : GastoUiEvent

    data class UpdateGasto(
       val gasto: Gasto
    ) : GastoUiEvent

    data class OnFechaChange(val fecha: String) : GastoUiEvent
    data class OnSuplidorChange(val suplidor: String) : GastoUiEvent
    data class OnNcfChange(val ncf: String) : GastoUiEvent
    data class OnItbisChange(val itbis: String) : GastoUiEvent
    data class OnMontoChange(val monto: String) : GastoUiEvent

    object ShowBottomSheet : GastoUiEvent
    object HideBottomSheet : GastoUiEvent

    object UserMessageShown : GastoUiEvent

}