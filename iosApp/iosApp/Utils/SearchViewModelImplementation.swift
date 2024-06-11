//
//  SearchViewModelImplementation.swift
//  iosApp
//
//  Created by marcjalkh on 29/02/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import shared
import SearchableList

open class SearchViewModelImplementation: SearchViewModel{
    override init(
        list: [SearchItem] = [],
        categorize: Bool = false,
        searchText: String = "",
        similarityCheck: @escaping (String, String) -> Double = { word, searchText in
            SharedCompareStringsUseCase().findSimilarity(x: String(word), y: searchText.lowercased())
        }) {
        super.init(
            list: list,
            categorize: categorize,
            searchText: searchText,
            similarityCheck: similarityCheck
        )
    }
}
