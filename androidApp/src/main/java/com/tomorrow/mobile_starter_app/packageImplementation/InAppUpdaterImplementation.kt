package com.tomorrow.mobile_starter_app.packageImplementation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.tomorrow.appupdate.InAppUpdater
import com.tomorrow.appupdate.StoreInfo
import com.tomorrow.appupdate.UpdateType
import com.tomorrow.mobile_starter_app.packageImplementation.mappers.AppUpdaterMapper
import com.tomorrow.mobile_starter_app.packageImplementation.mappers.StoreInfoMapper
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.GetAppConfigUseCase
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.GetUpdateTypeUseCase
import kotlinx.coroutines.launch

@Composable
fun InAppUpdaterImplementation() {
    var appInfo by remember { mutableStateOf<StoreInfo?>(null) }
    var updateType by remember { mutableStateOf(UpdateType.None) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = "update") {
        scope.launch {
            GetUpdateTypeUseCase()
                .getUpdateType().onSuccess {
                    updateType = AppUpdaterMapper().mapToEntity(it)
                }
        }
        appInfo = StoreInfoMapper().mapToEntity( GetAppConfigUseCase().get() )
    }

    return Column {
        InAppUpdater(
            storeInfo = appInfo,
            updateType = updateType
        )
    }
}