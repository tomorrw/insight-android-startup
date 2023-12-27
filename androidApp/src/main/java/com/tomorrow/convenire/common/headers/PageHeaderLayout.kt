package com.tomorrow.convenire.common.headers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.common.AppSeparator
import com.tomorrow.convenire.common.buttons.BackButton

/**
 * if no function was passed for onBackPress
 * the button will not be displayed
 * */
@Composable
fun PageHeaderLayout(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    onBackPress: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    content: @Composable () -> Unit,
) = Column(modifier.fillMaxHeight()) {
    CompositionLocalProvider(
        LocalDensity provides Density(LocalDensity.current.density, 1f)
    ) {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            onBackPress?.let {
                BackButton(onClick = { it() })
            }

            Column {
                title?.let {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = it, style = MaterialTheme.typography.headlineSmall)

                        AppSeparator(Modifier.width(72.dp))
                    }
                }
                subtitle?.let {
                    Text(
                        modifier = Modifier.padding(top = 7.dp),
                        text = it,
                        style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF6594BD))
                    )
                }
            }
        }
    }

    Column(modifier = Modifier.padding(contentPadding)) {
        content()
    }
}