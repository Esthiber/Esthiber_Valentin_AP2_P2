package edu.ucne.esthiber_valentin_ap2_p2.presentacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.esthiber_valentin_ap2_p2.data.remote.Resource
import edu.ucne.esthiber_valentin_ap2_p2.domain.model.Gasto
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun GastoScreen(
    viewModel: GastoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    GastoScreenBody(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastoScreenBody(
    state: GastoUiState,
    onEvent: (GastoUiEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(GastoUiEvent.UserMessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(GastoUiEvent.ShowBottomSheet) },
                modifier = Modifier.testTag("fab_add")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Gasto"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val gastos = state.listaGastos) {
                is Resource.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("loading")
                    )
                }
                is Resource.Success -> {
                    if (gastos.data.isNullOrEmpty()) {
                        Text(
                            text = "No hay gastos registrados",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .testTag("empty_message"),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = gastos.data,
                                key = { it.gastoId ?: 0 }
                            ) { gasto ->
                                GastoItem(
                                    gasto = gasto,
                                    onEdit = {
                                        onEvent(GastoUiEvent.getGasto(gasto.gastoId ?: 0))
                                        onEvent(GastoUiEvent.ShowBottomSheet)
                                    }
                                )
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = gastos.message ?: "Error desconocido",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("error_message"),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        if (state.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    onEvent(GastoUiEvent.HideBottomSheet)
                    onEvent(GastoUiEvent.OnFechaChange(""))
                    onEvent(GastoUiEvent.OnSuplidorChange(""))
                    onEvent(GastoUiEvent.OnNcfChange(""))
                    onEvent(GastoUiEvent.OnItbisChange(""))
                    onEvent(GastoUiEvent.OnMontoChange(""))
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (state.gastoId > 0) "Editar Gasto" else "Nuevo Gasto",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    OutlinedTextField(
                        value = state.gastoFecha,
                        onValueChange = { onEvent(GastoUiEvent.OnFechaChange(it)) },
                        label = { Text("Fecha") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_fecha"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.gastoSuplidor,
                        onValueChange = { onEvent(GastoUiEvent.OnSuplidorChange(it)) },
                        label = { Text("Suplidor") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_suplidor"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.gastoNcf,
                        onValueChange = { onEvent(GastoUiEvent.OnNcfChange(it)) },
                        label = { Text("NCF") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_ncf"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.gastoItbis,
                        onValueChange = { onEvent(GastoUiEvent.OnItbisChange(it)) },
                        label = { Text("ITBIS") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_itbis"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.gastoMonto,
                        onValueChange = { onEvent(GastoUiEvent.OnMontoChange(it)) },
                        label = { Text("Monto") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_monto"),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onEvent(GastoUiEvent.HideBottomSheet)
                                onEvent(GastoUiEvent.OnFechaChange(""))
                                onEvent(GastoUiEvent.OnSuplidorChange(""))
                                onEvent(GastoUiEvent.OnNcfChange(""))
                                onEvent(GastoUiEvent.OnItbisChange(""))
                                onEvent(GastoUiEvent.OnMontoChange(""))
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                if (state.gastoFecha.isNotBlank() &&
                                    state.gastoSuplidor.isNotBlank() &&
                                    state.gastoNcf.isNotBlank() &&
                                    state.gastoItbis.isNotBlank() &&
                                    state.gastoMonto.isNotBlank()
                                ) {
                                    val gasto = Gasto(
                                        gastoId = if (state.gastoId > 0) state.gastoId else null,
                                        fecha = state.gastoFecha,
                                        suplidor = state.gastoSuplidor,
                                        ncf = state.gastoNcf,
                                        itbis = state.gastoItbis.toDoubleOrNull() ?: 0.0,
                                        monto = state.gastoMonto.toDoubleOrNull() ?: 0.0
                                    )

                                    if (state.gastoId > 0) {
                                        onEvent(GastoUiEvent.UpdateGasto(gasto))
                                    } else {
                                        onEvent(GastoUiEvent.CrearGasto(gasto))
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_save"),
                            enabled = state.gastoFecha.isNotBlank() &&
                                    state.gastoSuplidor.isNotBlank() &&
                                    state.gastoNcf.isNotBlank() &&
                                    state.gastoItbis.isNotBlank() &&
                                    state.gastoMonto.isNotBlank()
                        ) {
                            Text(if (state.gastoId > 0) "Actualizar" else "Guardar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GastoItem(
    gasto: Gasto,
    onEdit: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("gasto_item_${gasto.gastoId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Suplidor: ${gasto.suplidor}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Fecha: ${gasto.fecha}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "NCF: ${gasto.ncf}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row {
                    Text(
                        text = "ITBIS: ${NumberFormat.getCurrencyInstance(Locale.US).format(gasto.itbis)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = " | Monto: ${NumberFormat.getCurrencyInstance(Locale.US).format(gasto.monto)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            IconButton(
                onClick = onEdit,
                modifier = Modifier.testTag("btn_edit_${gasto.gastoId}")
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar gasto"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GastoScreenPreview() {
    MaterialTheme {
        val state = GastoUiState( 
            showBottomSheet = true,
            isLoading = false,
            listaGastos = Resource.Success(
                listOf(
                    Gasto(
                        gastoId = 1,
                        fecha = "2024-11-05",
                        suplidor = "Supermercado Nacional",
                        ncf = "B0100000001",
                        itbis = 27.0,
                        monto = 150.0
                    ),
                    Gasto(
                        gastoId = 2,
                        fecha = "2024-11-04",
                        suplidor = "Ferretería Central",
                        ncf = "B0100000002",
                        itbis = 54.0,
                        monto = 300.0
                    )
                )
            )
        )
        GastoScreenBody(state) {}
    }
}