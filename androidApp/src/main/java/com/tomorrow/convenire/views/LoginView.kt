package com.tomorrow.convenire.views

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.common.Loader
import com.tomorrow.convenire.common.fields.AppTextField
import com.tomorrow.convenire.common.fields.PhoneVisualTransformation
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.permissions.RequestNotificationPermission
import org.koin.androidx.compose.getViewModel


@Composable
fun LoginView() = Column(
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
        .fillMaxHeight()
        .padding(horizontal = 16.dp)
) {
    val viewModel: RegisterViewModel = getViewModel()
    val navController = LocalNavController.current
    val context = LocalContext.current

    RequestNotificationPermission()

    LaunchedEffect(key1 = viewModel.state.error) {
        viewModel.state.error?.let {
            if (it.isNotEmpty()) Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Text(text = "Login", style = MaterialTheme.typography.headlineLarge)

    Text(
        text = "Please enter your ${if (viewModel.state.isLoginWithEmail) "email" else "phone number"} to login",
        style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
    )

    Column(Modifier.verticalScroll(rememberScrollState())) {
        Spacer(Modifier.height(48.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val phoneFocusRequester = remember { FocusRequester() }
            LaunchedEffect(
                key1 = "",
                key2 = viewModel.state.isLoginWithEmail
            ) { phoneFocusRequester.requestFocus() }
            if (viewModel.state.isLoginWithEmail) {
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(phoneFocusRequester),
                    value = viewModel.state.email,
                    onValueChange = {
                        viewModel.on(
                            if (viewModel.state.isLoginWithEmail)
                                RegisterEvent.EnteredEmail(
                                    it
                                )
                            else
                                RegisterEvent.EnteredPhone(
                                    it
                                )
                        )
                    },
                    label = "Email",
                    icon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Email
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        viewModel.on(RegisterEvent.ClearError)
                        viewModel.on(RegisterEvent.Login(onSuccess = {
                            navController.navigate(
                                AppRoute.OTP.generate()
                            )
                        }))
                    }),
                    visualTransformation = PhoneVisualTransformation
                )
            } else {
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
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Phone
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        viewModel.on(RegisterEvent.ClearError)
                        viewModel.on(RegisterEvent.Login(onSuccess = {
                            navController.navigate(
                                AppRoute.OTP.generate()
                            )
                        }))
                    }),
                    visualTransformation = PhoneVisualTransformation
                )
            }

        }

        Spacer(modifier = Modifier.height(56.dp))

        Button(
            onClick = {
                viewModel.on(RegisterEvent.Login(onSuccess = {
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
                "Login",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column {
            val interactionSource = remember { MutableInteractionSource() }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Don't have an account?  ",
                    style = MaterialTheme.typography.labelLarge.copy(MaterialTheme.colorScheme.secondary)
                )
                Text(
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        viewModel.on(RegisterEvent.ClearError)
                        navController.navigate(AppRoute.Register.generateExplicit(false))
                    },
                    text = "Register",
                    style = MaterialTheme.typography.labelLarge.copy(
                        MaterialTheme.colorScheme.surfaceVariant,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        viewModel.on(RegisterEvent.ClearError)
                        viewModel.on(RegisterEvent.LoginWithEmail)
                    },
                    text = "Click here",
                    style = MaterialTheme.typography.labelLarge.copy(
                        MaterialTheme.colorScheme.surfaceVariant,
                        textDecoration = TextDecoration.Underline
                    )
                )
                Text(
                    " to login with your ${if (!viewModel.state.isLoginWithEmail) "email" else "phone number"}",
                    style = MaterialTheme.typography.labelLarge.copy(MaterialTheme.colorScheme.secondary)
                )

            }
        }

        Spacer(Modifier.height(48.dp))

        BackHandler {
            navController.popBackStack()
        }
    }
}