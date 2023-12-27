package com.tomorrow.convenire.views

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomorrow.convenire.common.Loader
import com.tomorrow.convenire.common.fields.AppTextField
import com.tomorrow.convenire.common.headers.PageHeaderLayout
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.shared.domain.model.Speaker
import com.tomorrow.convenire.shared.domain.use_cases.AskQuestionUseCase
import com.tomorrow.convenire.shared.domain.use_cases.GetSessionByIdUseCase
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


sealed class AskLectureQuestionEvent {
    data class PostQuestion(val onSuccess: () -> Unit) : AskLectureQuestionEvent()
    data class QuestionEdited(val question: String) : AskLectureQuestionEvent()
    data class AnonymousChanged(val isAnonymous: Boolean) : AskLectureQuestionEvent()
}

data class AskLectureQuestionState(
    val title: String = "",
    val question: String = "",
    val error: String? = null,
    val speaker: Speaker? = null,
    val isLoading: Boolean = false,
    val isAnonymous: Boolean = false,
)

class AskLectureQuestionViewModel(private val lectureId: String) : ViewModel(), KoinComponent {
    var state by mutableStateOf(AskLectureQuestionState())

    init {
        viewModelScope.launch {
            GetSessionByIdUseCase()
                .getSession(lectureId)
                .collect {
                    state = state.copy(
                        title = it.title,
                        speaker = it.speakers.firstOrNull()
                    )
                }
        }
    }

    fun on(event: AskLectureQuestionEvent) {
        when (event) {
            is AskLectureQuestionEvent.PostQuestion -> {
                viewModelScope.launch {
                    state = state.copy(isLoading = true)
                    val success = AskQuestionUseCase().askQuestion(
                        lectureId,
                        state.question,
                        state.isAnonymous
                    )
                    if (success) event.onSuccess()
                    state = state.copy(
                        question = "",
                        isLoading = false,
                        error = if (success) null else "Something went wrong!"
                    )
                }
            }

            is AskLectureQuestionEvent.AnonymousChanged -> {
                state = state.copy(isAnonymous = event.isAnonymous)
            }

            is AskLectureQuestionEvent.QuestionEdited -> {
                state = state.copy(question = event.question)
            }
        }
    }
}


@Composable
fun AskLectureQuestionView(eventId: String) {
    val viewModel: AskLectureQuestionViewModel = getViewModel { parametersOf(eventId) }
    val navController = LocalNavController.current

    PageHeaderLayout(
        title = "Post Your Question",
        subtitle = "Ask the speaker an immediate question",
        onBackPress = { navController.popBackStack() }
    ) {
        val focusRequester = remember { FocusRequester() }
        val context = LocalContext.current

//        LaunchedEffect(Unit) { focusRequester.requestFocus() }
        LaunchedEffect(key1 = viewModel.state.error) {
            viewModel.state.error?.let {
                if (it.isNotEmpty()) Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            val maxChar = 260

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = viewModel.state.title, style = MaterialTheme.typography.titleLarge
            )

            viewModel.state.speaker?.getFullName()?.let {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "Dr. $it",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF959EAD)),
                )
            }

            Spacer(Modifier.height(8.dp))

            AppTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .focusRequester(focusRequester),
                value = viewModel.state.question,
                onValueChange = {
                    if (it.length <= maxChar) viewModel.on(
                        AskLectureQuestionEvent.QuestionEdited(
                            it
                        )
                    )
                },
                label = "Question",
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.on(
                        AskLectureQuestionEvent.PostQuestion(onSuccess =
                        {
                            Toast.makeText(
                                context,
                                "Question successfully posted",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        })
                    )
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                supportingText = {
                    Text(
                        text = "${viewModel.state.question.length} / $maxChar",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                }
            )

            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = viewModel.state.isAnonymous,
                    onCheckedChange = { viewModel.on(AskLectureQuestionEvent.AnonymousChanged(it)) }
                )

                Text("Stay Anonymous", modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { viewModel.on(AskLectureQuestionEvent.AnonymousChanged(viewModel.state.isAnonymous.not())) }
                )
            }

            Button(
                onClick = {
                    viewModel.on(AskLectureQuestionEvent.PostQuestion(onSuccess = {
                        Toast
                            .makeText(context, "Question successfully posted", Toast.LENGTH_LONG)
                            .show()
                    }))
                },
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .heightIn(min = 50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            ) {
                if (viewModel.state.isLoading) Loader() else Text(
                    "Submit",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}