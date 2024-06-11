//
//  CompaniesPage.swift
//  iosApp
//
//  Created by Yammine on 4/25/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver
import UiComponents
import SearchableList

struct CompaniesPage: View {
    @InjectedObject private var vm: CompaniesPageViewModel
    
    var body: some View {
        SearchableList(
            vm: vm,
            rowView: { item in
                NavigateTo {
                    CompanyPage(id: item.id)
                } label: {
                    DefaultListItem(item: item)
                }
                
            })
        .task { await vm.getCompanies() }
        .navigationTitle("Companies")
        .navigationBarTitleDisplayMode(.inline)
        .background(Color("Background"))
    }
}
