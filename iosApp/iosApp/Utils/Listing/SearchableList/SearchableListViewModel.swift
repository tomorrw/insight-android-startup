//
//  SearchableListViewModel.swift
//  iosApp
//
//  Created by marcjalkh on 09/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared

class SearchViewModel: ObservableObject {
    typealias SearchItemScore = (item: SearchItem, similarity: Double)
    typealias SearchList = [SearchItemScore]
    
    var originalList: [SearchItem] = []
    var categorized: Bool = false
    
    let searchDebouncer = Debouncer(timeInterval: 0.2)
    
    @Published var errorMessage: String? = nil
    @Published var isLoading = false
    @Published var displayedList: [CategoryListItem] = []
    @Published var searchText: String = "" {
        didSet {
            searchDebouncer.renewInterval()
            searchDebouncer.handler = {
                DispatchQueue.main.async {
                    if self.searchText == "" {
                        self.changeDisplayList(self.originalList, categorize: self.categorized)
                    } else {
                        var newList: [SearchItemScore] = self.originalList
                            .map { self.getItemScore($0) }
                            .filter { $0.similarity > (Double(self.searchText.count) / 20.0).coerceIn(0.0, 0.6) }
                        
                        newList = self.customSort(array: newList) { $0.similarity < $1.similarity }
                        self.changeDisplayList(
                            newList
                                .map { $0.item }
                            ,
                            categorize: self.categorized
                        )
                    }
                }
            }
        }
    }
    
    func customSort<T>(array: [T], comparator: (T, T) -> Bool) -> [T] {
        var sortedArray = array
        for i in 0..<sortedArray.count {
            for j in 0..<sortedArray.count - 1 - i {
                if comparator(sortedArray[j], sortedArray[j + 1]) {
                    let temp = sortedArray[j]
                    sortedArray[j] = sortedArray[j + 1]
                    sortedArray[j + 1] = temp
                }
            }
        }
        return sortedArray
    }
    
    init(list: [SearchItem], categorize: Bool = false, searchText: String) {
        self.categorized = categorize
        self.searchText = searchText
        DispatchQueue.main.async {
            self.changeOriginalList(list, categorize: categorize)
        }
    }
    
    @MainActor private func getItemScore(_ item: SearchItem) -> SearchItemScore {
        let searchableWords: EnumeratedSequence<Array<Substring>> = item.getSearchableString()
            .lowercased()
            .split(separator: " ")
            .enumerated()
        
        let wordScores: [Double] = searchableWords
            .map { (index, word) in
                let indexMultiplier: Double = 1.0 - (0.2 * Double(index))
                let similarity = CompareStringsUseCase.shared.findSimilarity(x: String(word), y: searchText.lowercased())
                return indexMultiplier * similarity
            }
        
        let similarity = wordScores.max() ?? 0.0
        
        return SearchItemScore(item: item, similarity: similarity)
    }
    
    @MainActor func changeOriginalList(_ list: [SearchItem], categorize cat: Bool = false) {
        originalList = list
        changeDisplayList(list, categorize: cat)
    }
    
    @MainActor func changeDisplayList(_ list: [SearchItem], categorize cat: Bool = false) {
        if list.isEmpty {
            self.displayedList = []
        }
        else{
            if cat {
                self.displayedList = list
                    .compactMap { $0.category }
                    .unique
                    .map { category in
                        return CategoryListItem(
                            category: category,
                            items: list.filter { $0.category == category }
                        )
                    }
            } else {
                self.displayedList = [CategoryListItem(category: SearchCategory("other"), items: list)]
            }
        }
    }
    
    @MainActor func refreshData() async {
        let temp = originalList
        changeOriginalList([], categorize: categorized)
        changeOriginalList(temp, categorize: categorized)
    }
    
    @MainActor func refreshAll() async {
        isLoading = true
        defer { isLoading = false }
        searchText = ""
        await refreshData()
    }
}
