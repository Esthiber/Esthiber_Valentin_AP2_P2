package edu.ucne.esthiber_valentin_ap2_p2.presentacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.esthiber_valentin_ap2_p2.data.dto.GastoRequest
import edu.ucne.esthiber_valentin_ap2_p2.domain.model.Gasto
import edu.ucne.esthiber_valentin_ap2_p2.domain.usecases.GetGastoByIdUseCase
import edu.ucne.esthiber_valentin_ap2_p2.domain.usecases.GetGastosUseCase
import edu.ucne.esthiber_valentin_ap2_p2.domain.usecases.PostGastoUseCase
import edu.ucne.esthiber_valentin_ap2_p2.domain.usecases.UpdateGastoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GastoViewModel @Inject constructor(
    private val getGastosUseCase: GetGastosUseCase,
    private val createGastoUseCase: PostGastoUseCase,
    private val getGastoByIdUseCase: GetGastoByIdUseCase,
    private val updateGastoUseCase: UpdateGastoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(GastoUiState(isLoading = true))
    val state: StateFlow<GastoUiState> = _state.asStateFlow()

    init {
        obtenerGastos()
    }

    fun obtenerGastos() {
        viewModelScope.launch {
            getGastosUseCase().collect { gastos ->
                _state.value = _state.value.copy(
                    listaGastos = gastos,
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: GastoUiEvent) {
        when (event) {
            is GastoUiEvent.CrearGasto -> CrearGasto(event.gasto)

            is GastoUiEvent.UpdateGasto -> UpdateGasto(event.gasto)

            is GastoUiEvent.getGasto -> getGasto(event.id)

            is GastoUiEvent.Load -> {
                obtenerGastos()
            }

            is GastoUiEvent.ShowBottomSheet -> {
                _state.value = _state.value.copy(showBottomSheet = true)
            }

            is GastoUiEvent.HideBottomSheet -> {
                _state.value = _state.value.copy(showBottomSheet = false)
            }

            is GastoUiEvent.OnFechaChange -> {
                _state.value = _state.value.copy(gastoFecha = event.fecha)
            }

            is GastoUiEvent.OnSuplidorChange -> {
                _state.value = _state.value.copy(gastoSuplidor = event.suplidor)
            }

            is GastoUiEvent.OnNcfChange -> {
                _state.value = _state.value.copy(gastoNcf = event.ncf)
            }

            is GastoUiEvent.OnItbisChange -> {
                _state.value = _state.value.copy(gastoItbis = event.itbis)
            }

            is GastoUiEvent.OnMontoChange -> {
                _state.value = _state.value.copy(gastoMonto = event.monto)
            }

            is GastoUiEvent.UserMessageShown -> {
                clearMessage()
            }
        }
    }

    private fun CrearGasto(gasto: Gasto) {
        viewModelScope.launch {
            val gastoReq = GastoRequest(
                fecha = gasto.fecha,
                suplidor = gasto.suplidor,
                ncf = gasto.ncf,
                itbis = gasto.itbis,
                monto = gasto.monto
            )
            when (val result = createGastoUseCase(gastoReq)) {
                is Gasto -> {
                    _state.update {
                        it.copy(
                            userMessage = "Gasto creado exitosamente",
                            gastoFecha = "",
                            gastoSuplidor = "",
                            gastoNcf = "",
                            gastoItbis = "",
                            gastoMonto = "",
                            showBottomSheet = false
                        )
                    }
                    obtenerGastos()
                }

                else -> {}
            }
        }
    }

    private fun UpdateGasto(gasto: Gasto) {
        viewModelScope.launch {
            when (val result = updateGastoUseCase(
                gasto.gastoId ?: 0,
                GastoRequest(
                    gasto.fecha, gasto.suplidor,
                    gasto.ncf, gasto.itbis, gasto.monto
                )
            )) {
                is Gasto -> {
                    _state.update {
                        it.copy(
                            userMessage = "Gasto actualizado exitosamente",
                            gastoFecha = "",
                            gastoSuplidor = "",
                            gastoNcf = "",
                            gastoItbis = "",
                            gastoMonto = "",
                            showBottomSheet = false
                        )
                    }
                    obtenerGastos()
                }
                else -> {}
            }
        }
    }

    private fun getGasto(id: Int) {
        viewModelScope.launch {
            when (val result = getGastoByIdUseCase(id)) {
                is Gasto -> {
                    _state.value = _state.value.copy(
                        gastoId = result.gastoId ?: 0,
                        gastoFecha = result.fecha,
                        gastoSuplidor = result.suplidor,
                        gastoNcf = result.ncf,
                        gastoItbis = result.itbis.toString(),
                        gastoMonto = result.monto.toString()
                    )
                }
            }
        }
    }

    private fun clearMessage() {
        _state.update { it.copy(userMessage = null) }
    }
}


