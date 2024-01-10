package com.tomorrow.convenire.common.view_models

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
import com.tomorrow.convenire.common.GeneralError
import com.tomorrow.convenire.common.Loader
import com.tomorrow.convenire.shared.data.data_source.utils.Loadable
import com.tomorrow.convenire.shared.data.data_source.utils.Loaded
import com.tomorrow.convenire.shared.data.data_source.utils.NotLoaded
import com.tomorrow.convenire.shared.domain.model.toUserFriendlyError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class ReadViewModel<D>(
    internal val load: () -> Flow<D>,
    internal val refresh: () -> Flow<D> = load,
) : ViewModel(), KoinComponent {
    var state by mutableStateOf(State<D>())
    val scope: CoroutineScope by inject()

    constructor(suspendLoad: suspend () -> D) : this(
        load = { flow { emit(suspendLoad()) } },
        refresh = { flow { emit(suspendLoad()) } },
    )

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
        state = state.copy(viewData = Loaded(d))
    }

    sealed class Event {
        data object OnRefresh : Event()
        data object Load : Event()
        data object ClearErrors : Event()
        data object LoadSilently : Event()
    }

    data class State<D>(
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val viewData: Loadable<D> = NotLoaded(),
        val error: String? = null,
    ) {
        fun copy(
            isLoading: Boolean = this.isLoading,
            isRefreshing: Boolean = this.isRefreshing,
            viewData: D,
            error: String? = this.error,
        ) = State(isLoading, isRefreshing, Loaded(viewData), error)
    }
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

    val viewData = viewModel.state.viewData

    if (viewModel.state.isLoading) loader()
    else if (viewData is Loaded) {
        view(viewData.get())
    } else if (viewModel.state.error != null) error(viewModel.state.error ?: "")
    else loader()
}