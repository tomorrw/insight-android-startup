//
//  SearchableList.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SearchCategory: Hashable, Equatable, Comparable, Identifiable {
    static func < (lhs: SearchCategory, rhs: SearchCategory) -> Bool {
        return lhs.text == rhs.text
    }
    
    static func == (lhs: SearchCategory, rhs: SearchCategory) -> Bool {
        return lhs.text == rhs.text
    }
    
    init(_ text: String, image: String? = nil) {
        self.text = text
        self.iconImage = image
        self.id = UUID()
    }
    
    let id: UUID
    let text: String
    let iconImage: String?
}

struct SearchItem: Identifiable, Equatable, Hashable {
    let id: String
    let title: String
    let description: String
    let image: String?
    var category: SearchCategory? = nil
    
    func getSearchableString() -> String {
        return title + " " + description
    }
}

struct CategoryListItem: Hashable, Equatable, Comparable, Identifiable {
    static func < (lhs: CategoryListItem, rhs: CategoryListItem) -> Bool {
        return lhs.category == rhs.category
    }
    
    static func == (lhs: CategoryListItem, rhs: CategoryListItem) -> Bool {
        return lhs.category == rhs.category
    }
    
    let id: UUID = UUID()
    let category: SearchCategory
    let items: [SearchItem]
}

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

extension Array where Element: Equatable {
    var unique: [Element] {
        var uniqueValues: [Element] = []
        forEach { item in
            guard !uniqueValues.contains(item) else { return }
            uniqueValues.append(item)
        }
        return uniqueValues
    }
}

struct SearchableList<ItemDetailPage: View>: View {
    @ObservedObject var vm: SearchViewModel
    @State private var isDisplayingError = false
    
    var searchPlaceholder: String? = nil
    var noResultText: String = "- Make sure that all words are spelled correctly. \n - Try different keywords. \n - Try more general keywords."
    
    @ViewBuilder var itemDetailPage: (SearchItem) -> ItemDetailPage
    
    var body: some View {
        ZStack {
            if vm.displayedList.isEmpty && vm.isLoading {
                List(0..<5) {item in
                    HStack(spacing: 12) {
                        Circle()
                            .fill(.gray.opacity(0.3))
                            .frame(width: 50, height: 50)
                        VStack(alignment: .leading, spacing: 12) {
                            RoundedRectangle(cornerRadius: 8)
                                .fill(.gray.opacity(0.3))
                                .frame(width: 100, height: 22)
                            RoundedRectangle(cornerRadius: 16)
                                .fill(.gray.opacity(0.3))
                                .frame(maxWidth: .infinity)
                                .frame(height: 22)
                        }
                    }
                    .padding(.vertical, 8)
                    .listRowBackground(Color("Background"))
                }
                .refreshable { await vm.refreshAll() }
                .listStyle(.plain)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            } else if vm.displayedList.isEmpty {
                VStack(spacing: 8) {
                    Image(systemName: "minus.magnifyingglass")
                        .resizable()
                        .frame(width: 40, height: 40)
                    
                    Text("No result for \"\(vm.searchText)\"")
                        .font(.custom("ReadexPro-Medium", size: 25))
                    
                    Text(noResultText)
                        .font(.custom("ReadexPro-Medium", size: 16))
                        .foregroundColor(.secondary)
                        .multilineTextAlignment(.center)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
            
            if vm.categorized {
                ExtractedView(vm: vm, searchPlaceholder: searchPlaceholder, itemDetailPage: itemDetailPage)
            } else {
                List(vm.displayedList.flatMap { $0.items } ) { item in
                    NavigateTo {
                        itemDetailPage(item)
                    } label: {
                        HStack(spacing: 12) {
                            if item.image != nil {
                                UrlImageView(urlString: item.image)
                                    .scaledToFill()
                                    .clipShape(Circle())
                                    .frame(width: 50, height: 50)
                            }
                            
                            VStack(alignment: .leading) {
                                Text(item.title)
                                    .font(.system(size: 18))
                                    .padding(.bottom, 2)
                                
                                Text(item.description)
                                    .foregroundColor(Color("Secondary"))
                                    .lineLimit(1)
                                    .font(.system(size: 14))
                            }
                        }
                        .padding(.vertical, 8)
                    }
                    .listRowBackground(Color("Background"))
                }
                .refreshable { await vm.refreshAll() }
                .searchable(
                    text: $vm.searchText,
                    placement: .navigationBarDrawer(displayMode: .always)
                )
                .listStyle(.plain)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color("Background"))
            }
        }
        .alert("Load Failed.", isPresented: $isDisplayingError, actions: { }, message: {
            Text(vm.errorMessage ?? "Something Went Wrong!")
        })
        .onReceive(vm.$errorMessage, perform: { error in
            guard error != nil && error != "" else {
                isDisplayingError = false
                return
            }
            isDisplayingError = true
        })
    }
}

struct ExtractedView<ItemDetailPage: View>: View {
    @ObservedObject var vm: SearchViewModel
    var searchPlaceholder: String? = nil
    @ViewBuilder var itemDetailPage: (SearchItem) -> ItemDetailPage
    
    var body: some View {
        List(vm.displayedList.map { $0.category }, id: \.id) { category in
            Section {
                ForEach(vm.displayedList.first(where: { $0.category == category })?.items ?? [], id: \.self) { item in
                    NavigateTo {
                        itemDetailPage(item)
                    } label: {
                        HStack(spacing: 12) {
                            if item.image != nil {
                                UrlImageView(urlString: item.image)
                                    .scaledToFill()
                                    .frame(width: 50, height: 50)
                                    .clipShape(Circle())
                            }
                            
                            VStack(alignment: .leading) {
                                Text(item.title)
                                
                                Text(item.description)
                                    .lineLimit(1)
                                    .foregroundColor(Color("Secondary"))
                            }
                        }
                        .padding(.vertical, 8)
                    }
                    .listRowBackground(Color("Background"))
                }
            } header: {
                HStack {
                    Text(category.text)
                    
                    UrlImageView(urlString: category.iconImage)
                        .clipShape(Circle())
                        .frame(width: 16, height: 16)
                        .padding(.leading, 4)
                }
            }
            .id(category.id)
        }
        .refreshable { await vm.refreshAll() }
        .searchable(
            text: $vm.searchText,
            placement: .navigationBarDrawer(displayMode: .always),
            prompt: searchPlaceholder ?? "Search"
        )
        .listStyle(.plain)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

//struct SearchableList_Previews: PreviewProvider {
//    static var previews: some View {
//        SearchableList()
//    }
//}
