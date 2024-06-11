//
//  CompaniesByCategoryPage.swift
//  iosApp
//
//  Created by Yammine on 4/25/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import SearchableList
import UiComponents

struct CompaniesByCategoryPage: View {
    @StateObject private var vm: CompaniesByCategoryPageViewModel
    
    init(id: String) {
        _vm = StateObject(wrappedValue: CompaniesByCategoryPageViewModel(categoryId: id))
    }
    
    var body: some View {
        SearchableList(
            vm: vm,
            rowView: { item in
                NavigateTo {
                    CompanyPage(id: item.id)
                } label: {
                    DefaultListItem(item: item)
                }
            }
        )
        .navigationBarTitleDisplayMode(.large)
    }
}
