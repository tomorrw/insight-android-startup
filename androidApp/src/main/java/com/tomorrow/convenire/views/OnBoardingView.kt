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
import com.tomorrow.carousel.OnBoardingCarousel
import com.tomorrow.carousel.OnBoardingItem
import com.tomorrow.convenire.feature_navigation.AppRoute
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
                    "Your Gateway to Continuous Medical Education! \uD83C\uDF93",
                    "Convenire brings you the medical conferences to the palm of your hands, with a wide range of features to help you get the most out of your conference experience!"
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
                    "Discover & Join verified conferences and workshops \uD83C\uDF10",
                    "Register to conferences and be notified of new workshops! Attend and claim your digital certificates!"
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
                    "Exhibitions, Deals,\n and a lot more... \uD83D\uDD14",
                    "Browse exhibitions, Develop connections, checkout posts & updates from organizers, and claim exclusive offers & promotions!"
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