//
//  CompaniesPage.swift
//  iosApp
//
//  Created by Yammine on 4/25/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver

struct CompaniesPage: View {
    @InjectedObject private var vm: CompaniesPageViewModel
    
    var body: some View {
        SearchableList(vm: vm) { item in
            CompanyPage(id: item.id)
        }
        .task { await vm.getCompanies() }
        .navigationTitle("Companies")
        .navigationBarTitleDisplayMode(.inline)
    }
}
