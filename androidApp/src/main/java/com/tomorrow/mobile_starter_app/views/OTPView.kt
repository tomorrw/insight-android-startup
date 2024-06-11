package com.tomorrow.mobile_starter_app.views

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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.tomorrow.components.fields.OtpTextField
import com.tomorrow.components.headers.PageHeaderLayout
import com.tomorrow.components.others.Loader
import com.tomorrow.mobile_starter_app.feature_navigation.AppRoute
import com.tomorrow.mobile_starter_app.launch.LocalNavController
import com.tomorrow.kmmProjectStartup.domain.model.Email
import com.tomorrow.kmmProjectStartup.domain.utils.PhoneNumber
import org.koin.androidx.compose.getViewModel

@Composable
fun OTPView() {
    val navController = LocalNavController.current
    val viewModel: RegisterViewModel = getViewModel()
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.state.error) {
        viewModel.state.error?.let {
            if (it.isNotEmpty()) Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    PageHeaderLayout(onBackPress = {
        navController.navigate(AppRoute.Register.generateExplicit(true)) {
            popUpTo(AppRoute.Register.generateExplicit(true)) {
                inclusive = true
            }
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Enter OTP", style = MaterialTheme.typography.headlineLarge)

            var loginField = remember(viewModel.state.phone) {
                PhoneNumber(viewModel.state.phone).number
            }

            if (viewModel.state.isLoginWithEmail) {

                loginField = Email(viewModel.state.email).value

            }

            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                        append("An ${if (viewModel.state.isLoginWithEmail) "Email" else "SMS"} will arrive shortly to \n")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.surfaceVariant)) {
                        append("$loginField ")
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                        append("containing your OTP")
                    }
                }
            )


            Spacer(Modifier.height(48.dp))

            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }


            OtpTextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
                otpText = viewModel.state.otp,
                enabled = !viewModel.state.isLoading,
                onOtpTextChange = { otp, isDone ->
                    viewModel.on(RegisterEvent.OtpEntered(otp))

                    if (isDone) viewModel.on(RegisterEvent.VerifyOtp(onSuccess = {
                        navController.navigate(AppRoute.Home.generate())
                    }))
                }
            )

            Spacer(Modifier.height(16.dp))


            if (viewModel.state.isLoading) Loader(Modifier.height(50.dp))
            else {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    viewModel.state.timeBeforeSendingAnotherOtp.let {
                        if (it != null) Text(
                            text = "Resend after $it sec.",
                            style = MaterialTheme.typography.labelLarge.copy(MaterialTheme.colorScheme.secondary)
                        ) else {
                            Text(
                                "Didn't receive code? ",
                                style = MaterialTheme.typography.labelLarge.copy(MaterialTheme.colorScheme.secondary)
                            )

                            val interactionSource = remember { MutableInteractionSource() }
                            Text(
                                modifier = Modifier.clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) { viewModel.on(RegisterEvent.ResendOtp) },
                                text = "Resend OTP",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Not your ${if (viewModel.state.isLoginWithEmail) "email" else "number"}? ",
                        style = MaterialTheme.typography.labelLarge.copy(MaterialTheme.colorScheme.secondary)
                    )
                    val interactionSource = remember { MutableInteractionSource() }
                    Text(
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.popBackStack()
                        },
                        text = "Change ${if (viewModel.state.isLoginWithEmail) "Email" else "Number"}",
                        style = MaterialTheme.typography.labelLarge.copy(
                            MaterialTheme.colorScheme.surfaceVariant,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }

            BackHandler {
                navController.navigate(AppRoute.Register.generateExplicit(true)) {
                    popUpTo(AppRoute.Register.generateExplicit(true)) {
                        inclusive = true
                    }
                }
            }
        }
    }
}