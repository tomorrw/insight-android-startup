//
//  SearchableList.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SearchableList<ItemDetailPage: View, Loader: View, EmptySearchPage: View, EmptyPage: View, ListView: View, HeaderView: View>: View {
    @ObservedObject var vm: SearchViewModel
    @State private var isDisplayingError = false
    var isSearchable: Bool
    var showSeperators: Bool
    @State var searchPlaceholder: String?
    @State var noResultText: String
    
    @ViewBuilder var itemDetailPage: (SearchItem) -> ItemDetailPage
    @ViewBuilder var loader: () -> Loader
    @ViewBuilder var emptySearchListView: (_ searchText: Binding<String>, _ noResult: Binding<String>) -> EmptySearchPage
    @ViewBuilder var listView: (_ item: SearchItem) -> ListView
    @ViewBuilder var customHeader: () -> HeaderView
    @ViewBuilder var emptyListView : () -> EmptyPage
    
    init(vm: SearchViewModel,
         isSearchable: Bool = true,
         showSeperators:Bool = true,
         searchPlaceholder: String? = nil,
         noResultText: String? = nil,
         itemDetailPage: @escaping (SearchItem) -> ItemDetailPage,
         loader: @escaping () -> Loader = { DefaultListLoader() } ,
         emptySearchListView: @escaping (_ searchText: Binding<String>, _ noResult: Binding<String>) -> EmptySearchPage = {s,n in DefaultEmptyList(searchText: s, noResultText: n) },
         emptyListView: @escaping () -> EmptyPage = { EmptyStateView ( title: "Nothing here.", text: "Stay tuned for more!" ) },
         listView: @escaping (_: SearchItem) -> ListView = { item in DefaultListItem(item: item) },
         customHeader: @escaping () -> HeaderView = { EmptyView() }
    ) {
        self.vm = vm
        self.isSearchable = isSearchable
        self.searchPlaceholder = searchPlaceholder
        self.noResultText = noResultText ?? "- Make sure that all words are spelled correctly. \n - Try different keywords. \n - Try more general keywords."
        self.itemDetailPage = itemDetailPage
        self.loader = loader
        self.emptySearchListView = emptySearchListView
        self.listView = listView
        self.customHeader = customHeader
        self.showSeperators = showSeperators
        self.emptyListView = emptyListView
    }
    
    var body: some View {
        VStack(spacing: 0) {
            customHeader()
            
            ZStack {
                if vm.displayedList.isEmpty && vm.isLoading {
                    loader()
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else if vm.displayedList.isEmpty && vm.searchText.isEmpty {
                    emptyListView()
                    .padding()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                }
                else if vm.displayedList.isEmpty {
                    emptySearchListView($vm.searchText, $noResultText)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                }
                else {
                    if vm.categorized {
                        ExtractedView(
                            vm: vm,
                            searchPlaceholder: searchPlaceholder,
                            itemDetailPage: itemDetailPage,
                            listView: listView,
                            showSeperators: self.showSeperators
                        )
                    } else {
                        List(vm.displayedList.flatMap { $0.items } ) { item in
                            NavigateTo {
                                itemDetailPage(item)
                            } label: {
                                listView(item)
                            }
                            .listRowBackground(Color("Background"))
                            .listRowSeparator(showSeperators ? .visible : .hidden)
                        }
                        .listStyle(.plain)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                        .background(Color("Background"))
                        
                    }
                }
            }
            .if(isSearchable, transform: { view in
                view.searchable(
                    text: $vm.searchText,
                    placement: .navigationBarDrawer(displayMode: .always),
                    prompt: searchPlaceholder ?? "Search"
                )
            })
        }
        .alert("Load Failed.", isPresented: $isDisplayingError, actions: { }, message: {
            Text(vm.errorMessage ?? "Something Went Wrong!")
        })
        .background(Color("Background"))
        .onReceive(vm.$errorMessage, perform: { error in
            guard error != nil && error != "" else {
                isDisplayingError = false
                return
            }
            isDisplayingError = true
        })
        .refreshable { await vm.refreshAll() }
    }
}

struct ExtractedView<ItemDetailPage: View, ListView: View>: View {
    @ObservedObject var vm: SearchViewModel
    var searchPlaceholder: String? = nil
    @ViewBuilder var itemDetailPage: (SearchItem) -> ItemDetailPage
    var listView: (_ item: SearchItem) -> ListView
    var showSeperators: Bool
    
    init(vm: SearchViewModel,
         searchPlaceholder: String? = nil,
         @ViewBuilder itemDetailPage: @escaping (SearchItem) -> ItemDetailPage,
         @ViewBuilder listView: @escaping (_: SearchItem) -> ListView = { item in DefaultListItem(item: item) },
         showSeperators: Bool = true
    ) {
        self.vm = vm
        self.searchPlaceholder = searchPlaceholder
        self.itemDetailPage = itemDetailPage
        self.listView = listView
        self.showSeperators = showSeperators
    }
    
    var body: some View {
        List(vm.displayedList.map { $0.category }, id: \.id) { category in
            Section {
                ForEach(vm.displayedList.first(where: { $0.category == category })?.items ?? []) { item in
                    NavigateTo {
                        itemDetailPage(item)
                    } label: {
                        listView(item)
                    }
                    .listRowBackground(Color("Background"))
                    .listRowSeparator(showSeperators ? .visible : .hidden)
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
        .listStyle(.plain)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
