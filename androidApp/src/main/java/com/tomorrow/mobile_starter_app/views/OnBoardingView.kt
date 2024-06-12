package com.tomorrow.mobile_starter_app.views

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
import com.tomorrow.mobile_starter_app.feature_navigation.AppRoute
import com.tomorrow.mobile_starter_app.launch.LocalNavController
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
                    "Demo OnBoarding",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
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