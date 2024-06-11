package com.tomorrow.convenire.common.buttons

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.common.handleLink
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.shared.domain.model.Action
import com.tomorrow.kmmProjectStartup.domain.model.toUserFriendlyError
import com.tomorrow.convenire.shared.domain.use_cases.PostActionUseCase
import kotlinx.coroutines.launch

@Composable
fun ActionButton(modifier: Modifier = Modifier, action: Action) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isButtonEnabled = remember { mutableStateOf(true) }
    val navController =  LocalNavController.current

    Button(
        onClick = {
            if (action.type == Action.ActionType.OpenLink) handleLink(
                action.url, navController, context
            )
            else scope.launch {
                isButtonEnabled.value = false
                var message = ""

                try {
                    PostActionUseCase().handle(action)
                        .onSuccess { message = it }
                        .onFailure { message = it.toUserFriendlyError() }

                } catch (e: Exception) {
                    message = "Something Went Wrong."
                }

                Toast.makeText(context, message, Toast.LENGTH_LONG).show()

                isButtonEnabled.value = true
            }
        },
        modifier = modifier
            .heightIn(min = 50.dp)
            .fillMaxWidth(),
        enabled = !action.isDisabled && isButtonEnabled.value,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (action.isPrimary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            action.label,
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary),
            textAlign = TextAlign.Center
        )
    }
}