package com.tomorrow.lda.common.view_models

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.tomorrow.lda.common.GeneralError
import com.tomorrow.lda.common.Loader
import com.tomorrow.lda.shared.domain.model.toUserFriendlyError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class ReadViewModel<D>(
    internal val load: () -> Flow<D>,
    internal val refresh: () -> Flow<D> = load,
) : ViewModel(), KoinComponent {
    var state by mutableStateOf(State<D>())
    val scope: CoroutineScope by inject()

    init {
        on(Event.Load)
    }

    fun on(event: Event) {
        Log.v("Read View Model Event", "$event")
        when (event) {
            Event.Load -> {
                scope.launch(Dispatchers.Main) {
                    state = state.copy(isLoading = true, error = null)
                    try {
                        load().collect {
                            onDataReception(it)
                            state = state.copy(isLoading = false)
                        }
                    } catch (e: Throwable) {
                        Log.e("Read View Model Error", "$e")
                        state = state.copy(error = e.toUserFriendlyError(), isLoading = false)
                    }
                }
            }

            Event.OnRefresh -> {
                scope.launch(Dispatchers.Main) {
                    state = state.copy(isRefreshing = true, error = null)
                    try {
                        refresh().collect {
                            onDataReception(it)
                            state = state.copy(isRefreshing = false)
                        }
                    } catch (e: Throwable) {
                        Log.e("Read View Model Error", "$e")
                        state = state.copy(error = e.toUserFriendlyError(), isRefreshing = false)
                    }
                }
            }

            Event.ClearErrors -> {
                state = state.copy(error = null)
            }

            Event.LoadSilently -> {
                scope.launch(Dispatchers.Main) {
                    state = state.copy(error = null)
                    try {
                        load().collect {
                            onDataReception(it)
                        }
                    } catch (e: Throwable) {
                        Log.e("Read View Model Error", "$e")
                        state = state.copy(error = e.toUserFriendlyError())
                    }
                }
            }
        }
    }

    open fun onDataReception(d: D) {
        state = state.copy(viewData = d)
    }

    sealed class Event {
        object OnRefresh : Event()
        object Load : Event()
        object ClearErrors : Event()
        object LoadSilently : Event()
    }

    data class State<D>(
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val viewData: D? = null,
        val error: String? = null,
    )
}

/**
 * has error handling and loading
 * */
@Composable
fun <D> DefaultReadView(
    viewModel: ReadViewModel<D>,
    loader: @Composable () -> Unit = { Loader() },
    error: @Composable (String) -> Unit = {
        GeneralError(
            modifier = Modifier.padding(16.dp),
            message = it,
            description = "Please check your internet connection and try again.",
            onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) },
        )
    },
    view: @Composable (D) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.state.error) {
        viewModel.state.error?.let {
            if (it.isNotEmpty()) Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    if (viewModel.state.isLoading) loader()
    else if (viewModel.state.viewData != null) viewModel.state.viewData?.let { view(it) }
        ?: loader()
    else if (viewModel.state.error != null) error(viewModel.state.error ?: "")
}