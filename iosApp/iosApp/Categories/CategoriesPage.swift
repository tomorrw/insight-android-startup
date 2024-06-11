//
//  CategoriesPage.swift
//  iosApp
//
//  Created by Yammine on 4/25/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import SearchableList
import UiComponents

struct CategoriesPage: View {
    @StateObject private var vm: SearchViewModel = CategoriesViewModel()
    
    var body: some View {
        SearchableList(
            vm: vm,
            rowView: { item in
                NavigateTo {
                    CompaniesByCategoryPage(id: item.id)
                        .navigationTitle(item.title)
                        .navigationBarTitleDisplayMode(.inline)
                } label: {
                    DefaultListItem(item: item)
                }
                
            }
        )
        .navigationTitle("Categories")
        .navigationBarTitleDisplayMode(.inline)
        .background(Color("Background"))
    }
}
