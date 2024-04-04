package com.tomorrow.convenire.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.tomorrow.convenire.R
import com.tomorrow.convenire.common.CustomToast
import com.tomorrow.convenire.common.PullToRefreshLayout
import com.tomorrow.convenire.common.dialogs.FullScreenPopUp
import com.tomorrow.convenire.common.headers.PageHeaderLayout
import com.tomorrow.convenire.common.vibratePhone
import com.tomorrow.convenire.common.view_models.DefaultReadView
import com.tomorrow.convenire.common.view_models.ReadViewModel
import com.tomorrow.convenire.feature_qr_code.rememberQrBitmapPainter
import com.tomorrow.convenire.shared.domain.model.ConfigurationData
import com.tomorrow.convenire.shared.domain.model.User
import com.tomorrow.convenire.shared.domain.use_cases.GetConfigurationUseCase
import com.tomorrow.convenire.shared.domain.use_cases.GetUserUseCase
import com.tomorrow.convenire.shared.domain.use_cases.LiveNotificationListenerUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import org.koin.androidx.compose.koinViewModel
import java.util.*

class MyQrViewData(
    val user: User,
    val configurationData: ConfigurationData?
)

class MyQrViewModel : ReadViewModel<MyQrViewData>(
    load = {
        (GetConfigurationUseCase().getTicketInfo() as Flow<ConfigurationData?>).catch { emit(null) }
            .combine(GetUserUseCase().getUser()) { configurationData, user ->
                try {
                    user.notificationTopics.map { Firebase.messaging.subscribeToTopic(it) }
                } catch (e: Exception) {
                    Log.e(
                        "Firebase subscription",
                        "failed to subscribe to user topic $e"
                    )
                }
                MyQrViewData(user, configurationData)
            }
    },
    refresh = {
        (GetConfigurationUseCase().getTicketInfo() as Flow<ConfigurationData?>).catch { emit(null) }
            .combine(GetUserUseCase().getUser()) { configurationData, user ->
                MyQrViewData(user, configurationData)
            }
    },
    onDismiss = { LiveNotificationListenerUseCase().stopListening() }

) {
    val notificationStatus = mutableStateOf(true)
    val notificationMessage = mutableStateOf("")

    fun startListening(id: String) {
        val liveNotify = LiveNotificationListenerUseCase()

        try {
            liveNotify.startListening(id) {
                it.getOrNull()?.let { notify ->
                    notificationMessage.value = notify.message
                    notificationStatus.value = notify.result
                }
            }
        } catch (e: Exception) {
            Log.e("LiveNotificationListenerUseCase", "Error startListening $e")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyQrView() {
    val viewModel: MyQrViewModel = koinViewModel()
    val context = LocalContext.current

    val shouldNotify = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel.notificationMessage.value) {
        if (viewModel.notificationMessage.value.isNotEmpty()) {
            context.vibratePhone()
            shouldNotify.value = true
        }
    }
    LaunchedEffect(key1 = shouldNotify.value) {
        if (!shouldNotify.value) {
            viewModel.notificationMessage.value = ""
        }
    }

    val shouldPopup = remember { mutableStateOf(false) }

    DefaultReadView(viewModel = viewModel) { ticket ->
        LaunchedEffect(key1 = "") {
            viewModel.startListening(ticket.user.id)
        }
        val qrCode = remember {
            mutableStateOf(ticket.user.generateQrCodeString())
        }
        FullScreenPopUp(shouldPopup) {
            Column(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(4.dp)
            ) {
                Image(
                    painter = rememberQrBitmapPainter(
                        content = qrCode.value,
                        size = 400.dp,
                        color = Color(0xFF113F67),
                        background = Color.Transparent
                    ),
                    contentDescription = "qr",
                )
            }
        }
        PageHeaderLayout(
            title = "My QR", subtitle = "Welcome Back ${ticket.user.fullName.getFormattedName()}"

        ) {
            val eventDate: String? =
                remember(key1 = ticket) { ticket.configurationData?.getFormattedDate() }

            LaunchedEffect(key1 = qrCode.value) {
                delay(30000)
                qrCode.value = ticket.user.generateQrCodeString()
            }

            CompositionLocalProvider(
                LocalDensity provides Density(LocalDensity.current.density, 1f)
            ) {
                PullToRefreshLayout(
                    state = rememberPullRefreshState(
                        refreshing = viewModel.state.isRefreshing,
                        onRefresh = {
                            viewModel.on(ReadViewModel.Event.OnRefresh)
                            qrCode.value = ticket.user.generateQrCodeString()
                        }
                    ),
                    isRefreshing = viewModel.state.isRefreshing
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        if (ticket.configurationData?.showTicket == true) Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.background),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Column(
                                Modifier
                                    .padding(horizontal = 24.dp)
                                    .padding(top = 24.dp)
                                    .padding(bottom = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = if (ticket.configurationData.subTitle.isNullOrEmpty()) Arrangement.Center else Arrangement.SpaceBetween
                                ) {
                                    val style = LocalTextStyle.current
                                    Text(
                                        text = ticket.configurationData.title,
                                        style = style.copy(
                                            letterSpacing = style.fontSize.times(0.2f),
                                            fontSize = style.fontSize.times(if (ticket.configurationData.hasDate && !ticket.configurationData.subTitle.isNullOrEmpty()) 1f else 1.25f),
                                            fontFamily = FontFamily(
                                                Font(R.font.ibmplexmono_regular)
                                            ),
                                            color = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    )
                                    ticket.configurationData.subTitle?.let {
                                        Text(
                                            text = it,
                                            style = style.copy(
                                                letterSpacing = style.fontSize.times(0.2f),
                                                fontFamily = FontFamily(
                                                    Font(R.font.ibmplexmono_regular)
                                                ),
                                                fontSize = style.fontSize.times(if (ticket.configurationData.hasDate) 1f else 1.25f),
                                                color = MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        )
                                    }
                                }

                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    eventDate?.forEach {
                                        Text(
                                            text = "$it",
                                            style = LocalTextStyle.current.copy(
                                                Color(0xFF959EAD), fontFamily = FontFamily(
                                                    Font(R.font.ibmplexmono_regular)
                                                )
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(36.dp))

                                Column(
                                    Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .padding(4.dp)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = { shouldPopup.value = true },
                                            )
                                        }
                                ) {
                                    Image(
                                        painter = rememberQrBitmapPainter(
                                            content = qrCode.value,
                                            size = 200.dp,
                                            color = Color(0xFF113F67),
                                            background = Color.White
                                        ),
                                        contentDescription = "qr",
                                    )
                                }
                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = ticket.user.fullName.getFullName(),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        MaterialTheme.colorScheme.onSurface
                                    )
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                ticket.configurationData.status?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            letterSpacing = MaterialTheme.typography.headlineSmall.fontSize * 0.2,
                                            fontFamily = FontFamily(Font(R.font.ibmplexmono_regular))
                                        )
                                    )
                                }
                            }

                            TicketSeparator()

                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .padding(bottom = 24.dp)
                                    .padding(top = 12.dp),
                                text = ticket.configurationData.description,
                                style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center)
                            )
                        } else Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Column(
                                Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(4.dp)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = { shouldPopup.value = true },
                                        )
                                    }
                            ) {
                                Image(
                                    painter = rememberQrBitmapPainter(
                                        content = qrCode.value,
                                        size = 200.dp,
                                        color = Color(0xFF113F67),
                                        background = Color.White
                                    ),
                                    contentDescription = "qr",
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = ticket.configurationData?.description
                                    ?: "Your Digital Identity",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }

        CustomToast(
            modifier = Modifier.zIndex(11f),
            message = viewModel.notificationMessage.value,
            isShown = shouldNotify,
            duration = 5000,
            icon = if (viewModel.notificationStatus.value)
                painterResource(id = R.drawable.waving_foreground)
            else painterResource(id = R.drawable.cross_error_foreground),
            iconColor = if (viewModel.notificationStatus.value) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.error,
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
