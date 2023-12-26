package com.tomorrow.lda.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.UUID

class Page(val title: String, val sections: List<Section>)

@Composable
fun PageTabDisplay(
    pages: List<Page>
) {
    val selectedTab = rememberSaveable { mutableStateOf(0) }
    val tabs: List<TabInfo> = remember {
        pages.map {
            TabInfo(title = it.title) {
                LazyColumn(contentPadding = PaddingValues(top = 40.dp)) {
                    items(it.sections) { section ->
                        SectionDisplay(section = section)
                        Spacer(Modifier.height(32.dp))
                    }
                    item { Spacer(Modifier.height(42.dp)) }
                }
            }
        }
    }

    Column(
        Modifier
            .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
            .background(Color(0xFFFFFFFF))
            .fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTab.value,
            containerColor = Color.Transparent,
            divider = {},
            indicator = {
                if (tabs.size > 1)
                    Box(
                        Modifier
                            .tabIndicatorOffset(it[selectedTab.value])
                            .fillMaxWidth()
                            .height(4.dp)
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(topEnd = 4.dp, topStart = 4.dp))
                            .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    )
            }) {
            tabs.forEachIndexed { i, info ->
                Tab(
                    selected = selectedTab.value == i,
                    onClick = { selectedTab.value = i },
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ) { Text(info.title, modifier = Modifier.padding(bottom = 14.dp, top = 18.dp)) }
            }
        }

        Column {
            tabs[selectedTab.value].content()
        }
    }
}

private data class TabInfo(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val content: @Composable () -> Unit,
)