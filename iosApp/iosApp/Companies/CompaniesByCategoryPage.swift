//
//  CompaniesByCategoryPage.swift
//  iosApp
//
//  Created by Yammine on 4/25/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct CompaniesByCategoryPage: View {
    @StateObject private var vm: CompaniesByCategoryPageViewModel
    
    init(id: String) {
        _vm = StateObject(wrappedValue: CompaniesByCategoryPageViewModel(categoryId: id))
    }
    
    var body: some View {
        SearchableList(vm: vm) { item in
            CompanyPage(id: item.id)
        }
        .navigationBarTitleDisplayMode(.large)
    }
}

