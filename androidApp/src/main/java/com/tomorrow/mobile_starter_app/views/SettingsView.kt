package com.tomorrow.mobile_starter_app.views

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.tomorrow.components.buttons.ColorThemeRadioButton
import com.tomorrow.components.dialogs.CustomAlertDialog
import com.tomorrow.components.headers.PageHeaderLayout
import com.tomorrow.mobile_starter_app.packageImplementation.mappers.ColorThemePresentationModelMapper
import com.tomorrow.mobile_starter_app.R
import com.tomorrow.mobile_starter_app.feature_navigation.AppRoute
import com.tomorrow.mobile_starter_app.launch.LocalNavController
import com.tomorrow.kmmProjectStartup.domain.model.toUserFriendlyError
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.ColorThemeUseCase
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.LogoutUseCase
import kotlinx.coroutines.launch

@Composable
fun SettingsView() {
    val navController = LocalNavController.current

    PageHeaderLayout(
        title = "Profile Settings",
        onBackPress = { navController.popBackStack() }
    ) {
        val isLoading = remember { mutableStateOf(false) }
        val isLogoutVisible = remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val uiTheme = ColorThemeUseCase().getColorTheme().collectAsState()
        Spacer(Modifier.height(24.dp))

        Column {
            Text(
                text = "COLOR THEME:",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier.padding(start = 8.dp)
            )

            ColorThemeRadioButton(
                onSelectedTheme = ColorThemePresentationModelMapper().mapFromEntity(uiTheme.value),
                onSelect = {
                    ColorThemeUseCase().setColorTheme(
                        ColorThemePresentationModelMapper().mapToEntity(it)
                    )
                })
        }

        Spacer(Modifier.height(24.dp))

        Column(
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .clickable { isLogoutVisible.value = true }
                    .padding(horizontal = 16.dp)
                    .heightIn(min = 50.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.round_logout_24),
                    contentDescription = "Log out Button",
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    "LOG OUT",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge.copy(MaterialTheme.colorScheme.error),
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.surface)
            )

            Row(
                modifier = Modifier
                    .clickable {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://api.convenire.app/request-account-deletion"),
                        )
                        try {
                            context.startActivity(intent)
                        } catch (e: Throwable) {
                            Toast
                                .makeText(context, e.toUserFriendlyError(), Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    .padding(horizontal = 16.dp)
                    .heightIn(min = 50.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Delete Account & Archive Data",
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    "Delete Account & Archive Data".uppercase(),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge.copy(MaterialTheme.colorScheme.error),
                )
            }
        }

        if (isLogoutVisible.value) CustomAlertDialog(
            title = "Logout?",
            description = "Are you sure you want to log out? all your changes will be lost.",
            ctaButtonText = "LOG OUT",
            onCTAClick = {
                scope.launch {
                    isLoading.value = true
                    LogoutUseCase().logout()
                        .onSuccess {
                            navController.navigate(AppRoute.OnBoarding.generate())
                            Firebase.messaging.deleteToken()
                        }
                        .onFailure {
                            Toast.makeText(
                                context,
                                it.toUserFriendlyError(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    isLogoutVisible.value = false
                    isLoading.value = false
                }
            },
            ctasEnabled = !isLoading.value,
            isDismissible = true,
            isDismissibleOnBack = true,
            onDismiss = { isLogoutVisible.value = false },
            dismissButtonText = "CANCEL",
            icon = {
                if (isLoading.value) CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(24.dp)
                ) else Icon(
                    painter = painterResource(R.drawable.round_logout_24),
                    contentDescription = "Log out Button",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}

@Composable
private fun TicketSeparator() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val surface = MaterialTheme.colorScheme.surface

        Canvas(
            Modifier
                .size(32.dp)
        ) {
            drawArc(
                color = surface,
                startAngle = -90f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(-size.width / 2f, 0f),
                size = Size(width = size.width, height = size.height),
            )
        }

        val density = LocalDensity.current
        val dashWidthInPx = with(density) { 12.dp.toPx() }
        val pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(
                dashWidthInPx,
                dashWidthInPx
            ), phase = 0.0f
        )

        Canvas(
            Modifier
                .weight(1f)
                .height(2.dp)
        ) {
            drawLine(
                color = Color(0xFFDAE6F1),
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                pathEffect = pathEffect
            )
        }

        Canvas(
            Modifier
                .size(32.dp)
        ) {
            drawArc(
                color = surface,
                startAngle = 90f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(size.width / 2f, 0f),
                size = Size(width = size.width, height = size.height),
            )
        }
    }
}