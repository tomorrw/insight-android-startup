package com.tomorrow.convenire.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.tomorrow.convenire.common.Loader
import com.tomorrow.convenire.common.fields.AppTextField
import com.tomorrow.convenire.common.fields.PhoneVisualTransformation
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.permissions.RequestNotificationPermission
import com.tomorrow.convenire.shared.domain.model.toUserFriendlyError
import com.tomorrow.convenire.shared.domain.use_cases.LoginUseCase
import com.tomorrow.convenire.shared.domain.use_cases.RegisterUseCase
import com.tomorrow.convenire.shared.domain.use_cases.VerifyOTPUseCase
import com.tomorrow.convenire.shared.domain.utils.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegisterViewModel : ViewModel(), KoinComponent {
    var state by mutableStateOf(RegisterState())

    init {
        state = state.copy(phone = "")
    }

    private val scope: CoroutineScope by inject()
    private var uuid: UUID? = null

    fun on(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EnteredEmail -> state = state.copy(email = event.email)
            is RegisterEvent.EnteredFirstName -> state = state.copy(firstName = event.name)
            is RegisterEvent.EnteredLastName -> state = state.copy(lastName = event.name)
            is RegisterEvent.EnteredPhone -> state = state.copy(
                phone = event.phone.replace(
                    Regex("\\."),
                    ""
                )
            )

            is RegisterEvent.Register -> {
                scope.launch(Dispatchers.Main) {
                    state = state.copy(isLoading = true, error = null)
                    RegisterUseCase().register(
                        state.firstName,
                        state.lastName,
                        state.email,
                        state.phone
                    )
                        .onFailure {
                            state = state.copy(error = it.toUserFriendlyError())
                        }
                        .onSuccess {
                            uuid = it
                            event.onSuccess()
                        }
                    state = state.copy(isLoading = false)
                }
            }

            is RegisterEvent.OtpEntered -> {
                state = state.copy(otp = event.otp)
            }

            is RegisterEvent.VerifyOtp -> {

                scope.launch(Dispatchers.Main) {
                    state = state.copy(isLoading = true, error = null)
                    VerifyOTPUseCase().verify(
                        phoneNumber = if (state.isLoginWithEmail) null else state.phone,
                        if (state.isLoginWithEmail) state.email else null,
                        state.otp
                    )
                        .onSuccess {
                            event.onSuccess()
                            state = RegisterState()

                            try {
                                Firebase.messaging.subscribeToTopic("user_registered")
                                Firebase.messaging.subscribeToTopic("user_${it.id}")
                                it.notificationTopics.map { topic ->
                                    Firebase.messaging.subscribeToTopic(topic)
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    "Firebase subscription",
                                    "failed to subscribe to user topic $e"
                                )
                            }
                            // reset
                        }
                        .onFailure {
                            state = state.copy(error = it.toUserFriendlyError(), otp = "")
                        }
                    state = state.copy(isLoading = false)
                }
            }

            is RegisterEvent.ResendOtp -> {
                scope.launch(Dispatchers.Main) {
                    state = state.copy(isLoading = true, error = null)

                    if (state.isLoginWithEmail) {
                        LoginUseCase().login(null, email = state.email)
                            ?.onFailure { state = state.copy(error = it.toUserFriendlyError()) }
                            ?.onSuccess {
                                uuid = it
                            }
                    } else {
                        LoginUseCase().login(phoneNumber = state.phone, null)
                            ?.onFailure { state = state.copy(error = it.toUserFriendlyError()) }
                            ?.onSuccess {
                                uuid = it
                            }
                    }

                    state = state.copy(isLoading = false)
                }
            }

            is RegisterEvent.Login -> {
                scope.launch(Dispatchers.Main) {
                    state = state.copy(isLoading = true, error = null)

                    if (state.isLoginWithEmail) {
                        LoginUseCase().login(null, email = state.email)
                            ?.onFailure { state = state.copy(error = it.toUserFriendlyError()) }
                            ?.onSuccess {
                                uuid = it
                                event.onSuccess()
                            }
                    } else {
                        LoginUseCase().login(phoneNumber = state.phone, null)
                            ?.onFailure { state = state.copy(error = it.toUserFriendlyError()) }
                            ?.onSuccess {
                                uuid = it
                                event.onSuccess()
                            }
                    }
                    state = state.copy(isLoading = false)
                }
            }

            is RegisterEvent.LoginWithEmail -> {
                state = state.copy(isLoginWithEmail = !state.isLoginWithEmail)
            }

            RegisterEvent.ClearError -> {
                state = state.copy(error = null)
            }
        }
    }

    private fun countDownTillNextOtp() {
        val time = state.timeBeforeSendingAnotherOtp ?: return

        scope.launch {
            delay(1000)
            state = state.copy(timeBeforeSendingAnotherOtp = if (time == 0) null else time - 1)
            countDownTillNextOtp()
        }
    }
}

data class RegisterState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val otp: String = "",
    val timeBeforeSendingAnotherOtp: Int? = null,
    val isLoginWithEmail: Boolean = false
)

sealed class RegisterEvent {
    data class EnteredFirstName(val name: String) : RegisterEvent()
    data class EnteredLastName(val name: String) : RegisterEvent()
    data class EnteredEmail(val email: String) : RegisterEvent()
    data class EnteredPhone(val phone: String) : RegisterEvent()
    data class Register(val onSuccess: () -> Unit) : RegisterEvent()
    data class Login(val onSuccess: () -> Unit) : RegisterEvent()
    data class OtpEntered(val otp: String) : RegisterEvent()
    data class VerifyOtp(val onSuccess: () -> Unit) : RegisterEvent()
    object ClearError : RegisterEvent()
    object ResendOtp : RegisterEvent()
    object LoginWithEmail : RegisterEvent()
}

@Composable
fun RegisterView(focusPhoneNumber: Boolean? = null) = Column(
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
        .fillMaxHeight()
        .padding(horizontal = 16.dp)
) {
    val viewModel: RegisterViewModel = getViewModel()
    val focusManager = LocalFocusManager.current
    val navController = LocalNavController.current

    RequestNotificationPermission()

    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.state.error) {
        viewModel.state.error?.let {
            if (it.isNotEmpty()) Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Text(text = "Register", style = MaterialTheme.typography.headlineLarge)
    Text(
        text = "Please enter the following fields",
        style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
    )

    Column(Modifier.verticalScroll(rememberScrollState())) {
        Spacer(Modifier.height(48.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val focusRequester = remember { FocusRequester() }
            val lastNameFocusRequester = remember { FocusRequester() }
            val phoneFocusRequester = remember { FocusRequester() }

            if (focusPhoneNumber != true) LaunchedEffect(Unit) { focusRequester.requestFocus() }
            else LaunchedEffect(focusPhoneNumber) { phoneFocusRequester.requestFocus() }

            AppTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = viewModel.state.firstName,
                onValueChange = { viewModel.on(RegisterEvent.EnteredFirstName(it)) },
                label = "First Name",
                icon = Icons.Default.Person,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(lastNameFocusRequester),
                value = viewModel.state.lastName,
                onValueChange = { viewModel.on(RegisterEvent.EnteredLastName(it)) },
                label = "Last Name",
                icon = Icons.Default.Person,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(phoneFocusRequester),
                value = viewModel.state.phone,
                onValueChange = {
                    viewModel.on(
                        RegisterEvent.EnteredPhone(
                            it
                        )
                    )
                },
                label = "Phone",
                icon = Icons.Default.Phone,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Phone
                ),
                visualTransformation = PhoneVisualTransformation
            )
            AppTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.state.email,
                onValueChange = { viewModel.on(RegisterEvent.EnteredEmail(it)) },
                label = "Email Address (optional)",
                icon = Icons.Default.Email,
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.on(RegisterEvent.Register(onSuccess = {
                        navController.navigate(
                            AppRoute.OTP.generate()
                        )
                    }))
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }

        Spacer(modifier = Modifier.height(56.dp))

        Button(
            onClick = {
                viewModel.on(RegisterEvent.Register(onSuccess = {
                    navController.navigate(
                        AppRoute.OTP.generate()
                    )
                }))
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp),
            enabled = !viewModel.state.isLoading,
            shape = RoundedCornerShape(8.dp),
        ) {
            if (viewModel.state.isLoading) Loader() else Text(
                "Register",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Already have an account?  ",
                style = MaterialTheme.typography.labelLarge.copy(MaterialTheme.colorScheme.secondary)
            )
            val interactionSource = remember { MutableInteractionSource() }
            Text(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    viewModel.on(RegisterEvent.ClearError)
                    navController.navigate(AppRoute.Login.generate())
                },
                text = "Login",
                style = MaterialTheme.typography.labelLarge.copy(
                    MaterialTheme.colorScheme.surfaceVariant,
                    textDecoration = TextDecoration.Underline
                )
            )
        }

        Spacer(Modifier.height(48.dp))
    }
}