package com.tomorrow.convenire.packageImplementation

import com.tomorrow.kmmProjectStartup.domain.use_cases.CompareStringsUseCase
import com.tomorrow.listdisplay.ListDisplayItemInterface
import com.tomorrow.listdisplay.ListDisplayReadViewModel
import kotlinx.coroutines.flow.Flow

open class ListDisplayReadViewModelImplementation<Item : ListDisplayItemInterface>(
    load: () -> Flow<List<Item>>,
    refresh: () -> Flow<List<Item>>,
    emptyCheck: (List<Item>) -> Boolean = { it.isEmpty() },
) : ListDisplayReadViewModel<Item>(
    load = load,
    refresh = refresh,
    emptyCheck = emptyCheck,
    searchAlgorithm = { query, searchField ->
        CompareStringsUseCase.findSimilarity(
            query, searchField
        )
    }
)