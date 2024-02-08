package com.tomorrow.convenire.common.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.shared.domain.model.ColorTheme


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorThemeRadioButton(
    modifier: Modifier = Modifier,
    onSelectedTheme: ColorTheme?,
    onSelect: (ColorTheme) -> Unit
) {
    var selected by remember { mutableStateOf(onSelectedTheme) }


    FlowRow(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ColorTheme.values().map{
            Row {
                RadioButton(
                    selected = onSelectedTheme == it,
                    onClick = { selected = it; onSelect(it) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.secondary
                    )
                )

                Text(
                    text = it.name,
                    color = if (onSelectedTheme == it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .clickable(onClick = { selected = it; onSelect(it) })
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}