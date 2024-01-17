package com.tomorrow.convenire.feature_in_app_update

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.common.IntentSenderForResultStarter
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.tomorrow.convenire.feature_in_app_update.components.FlexibleUpdateDialog
import com.tomorrow.convenire.feature_in_app_update.components.ForceUpdateDialog
import com.tomorrow.convenire.shared.domain.model.AppConfig
import com.tomorrow.convenire.shared.domain.use_cases.GetAppConfig
import com.tomorrow.convenire.shared.domain.use_cases.GetUpdateTypeUseCase
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

private fun AppUpdateManager.startUpdate(
    intentLauncher: ActivityResultLauncher<IntentSenderRequest>,
    updateInfo: AppUpdateInfo,
    updateType: Int
) {
    startUpdateFlowForResult(
        updateInfo, updateType, intentLauncher.starter(), 0
    )
}


private fun ActivityResultLauncher<IntentSenderRequest>.starter(): IntentSenderForResultStarter =
    IntentSenderForResultStarter { intent, _, fillInIntent, flagsMask, flagsValue, _, _ ->
        launch(
            IntentSenderRequest.Builder(intent).setFillInIntent(fillInIntent)
                .setFlags(flagsValue, flagsMask).build()
        )
    }

@Composable
fun InAppUpdater(
    appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(LocalContext.current),
) {
    val intentLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { if (it.resultCode == Activity.RESULT_CANCELED) exitProcess(0) })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val isForceUpdateDialogVisible = rememberSaveable { mutableStateOf(false) }
    val isFlexibleUpdateDialogVisible = rememberSaveable { mutableStateOf(false) }
    var appInfo by rememberSaveable { mutableStateOf<AppConfig?>(null) }

    LaunchedEffect(key1 = "update") {
        fun toggleDialog(updateType: GetUpdateTypeUseCase.UpdateType) {
            isFlexibleUpdateDialogVisible.value =
                updateType == GetUpdateTypeUseCase.UpdateType.Flexible
            isForceUpdateDialogVisible.value = updateType == GetUpdateTypeUseCase.UpdateType.Forced
        }

        scope.launch {
            GetUpdateTypeUseCase()
                .getUpdateType()
                .onSuccess { type ->
                    if (type == GetUpdateTypeUseCase.UpdateType.None) return@launch

                    appUpdateManager.appUpdateInfo.addOnSuccessListener {
                        if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                            appUpdateManager.startUpdate(intentLauncher, it, type.toAppUpdateType())
                        } else {
                            toggleDialog(type)
                        }
                    }.addOnFailureListener { toggleDialog(type) }
                }
        }
        appInfo = GetAppConfig().appConfig
    }

    when {
        isForceUpdateDialogVisible.value -> ForceUpdateDialog(
            onCTAClick = { context.openUpdatePageInPlayStore(appInfo?.updateUrl?.toUri()) },
            appName = appInfo?.name ?: "App"
        )

        isFlexibleUpdateDialogVisible.value -> FlexibleUpdateDialog(
            onDismiss = { isFlexibleUpdateDialogVisible.value = false },
            isDismissible = true,
            isDismissibleOnBack = true,
            onCTAClick = { context.openUpdatePageInPlayStore(appInfo?.updateUrl?.toUri()) },
            appName = context.getAppName()
        )
    }
}

private fun Context.openUpdatePageInPlayStore(stringUrl: Uri?) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            stringUrl ?: "https://convenire.tomorrow.services/".toUri()
        )
    )
}

private fun Context.getAppName(): String {
    return this.applicationInfo.loadLabel(this.packageManager).toString()
}

private fun GetUpdateTypeUseCase.UpdateType.toAppUpdateType(): Int =
    if (this == GetUpdateTypeUseCase.UpdateType.Forced) AppUpdateType.IMMEDIATE else AppUpdateType.FLEXIBLE