package com.tomorrow.convenire.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.R
import com.tomorrow.convenire.common.headers.PageHeaderLayout
import com.tomorrow.convenire.launch.LocalNavController

@Composable
fun GeneralError(
    modifier: Modifier = Modifier,
    message: String,
    description: String,
    buttonText: String? = "Try Again",
    onButtonClick: (() -> Unit)? = null,
    hasBackButton: Boolean = false
) {
    val navController = LocalNavController.current
    PageHeaderLayout(
        onBackPress = if (hasBackButton) {
            { navController.popBackStack() }
        } else null
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(R.drawable.baseline_error_outline_24),
                    contentDescription = "Error Icon",
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    message,
                    style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                )

                onButtonClick?.let {
                    Button(
                        onClick = onButtonClick,
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .fillMaxWidth()
                            .heightIn(min = 50.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            buttonText ?: "Try Again",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}