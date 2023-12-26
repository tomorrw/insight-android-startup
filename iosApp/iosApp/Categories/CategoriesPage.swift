//
//  CategoriesPage.swift
//  iosApp
//
//  Created by Yammine on 4/25/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct CategoriesPage: View {
    @StateObject private var vm: SearchViewModel = CategoriesViewModel()
    
    var body: some View {
        SearchableList(vm: vm) { item in
            CompaniesByCategoryPage(id: item.id)
                .navigationTitle(item.title)
                .navigationBarTitleDisplayMode(.inline)
        }
        .navigationTitle("Categories")
        .navigationBarTitleDisplayMode(.inline)
    }
}

