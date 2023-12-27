package com.tomorrow.convenire.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.feature_on_boarding.OnBoardingCarousel
import com.tomorrow.convenire.feature_on_boarding.OnBoardingItem
import com.tomorrow.convenire.launch.LocalNavController
import kotlinx.coroutines.launch

@Composable
fun OnBoardingView() {
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val itemIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
    ) {
        OnBoardingCarousel(
            modifier = Modifier.fillMaxWidth(),
            listState = listState,
            items = listOf(
                OnBoardingItem(
                    "Discover and take part in our specialized Lectures \uD83D\uDCDA",
                    "Get involved in our conference sessions by registering your attendance and participating in Q&A. Get to know our guest speakers and get notified of their lectures.\n"
                ) {
                    Button(
                        onClick = { scope.launch { listState.animateScrollToItem(itemIndex.value + 1) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Next", style = MaterialTheme.typography.titleMedium)
                    }
                },
                OnBoardingItem(
                    "Collaborate & Connect with leading dental companies \uD83C\uDF10",
                    "Develop connections with our exhibition sponsors through posts & updates and elevate your dental practice with once-in-a-lifetime offers & promotions."
                ) {
                    Button(
                        onClick = { scope.launch { listState.animateScrollToItem(itemIndex.value + 1) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Next", style = MaterialTheme.typography.titleMedium)
                    }
                },
                OnBoardingItem(
                    "Stay in the loop with our latest updates \uD83D\uDD14",
                    "Sign up for your preferred lectures and turn on our notifications to get reminded of your custom schedule; the entire conference in the palm of your hand!"
                ) {
                    Button(
                        onClick = { navController.navigate(AppRoute.Login.generate()) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Get Started", style = MaterialTheme.typography.titleMedium)
                    }
                },
            ),
        )
    }
}