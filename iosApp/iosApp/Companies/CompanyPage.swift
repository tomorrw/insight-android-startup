//
//  CompanyPage.swift
//  iosApp
//
//  Created by Yammine on 4/25/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import DetailPage

struct CompanyPage: View {
    @StateObject private var vm: CompanyPageViewModel
    
    init(id: String) {
        _vm = StateObject(wrappedValue: CompanyPageViewModel(id: id))
    }
    
    var body: some View {
        DetailPage(
            vm: vm,
            customHeader: { SocialLinksDisplay(socialLinks: vm.socialLinks) },
            customBody: { PageTabDisplayView(
                pages: $vm.pages,
                colors: DefaultColors.pageTabDisplayColor) { SectionDisplayViewImplementation(section: $0) }
            }
        )
        .navigationBarTitleDisplayMode(.inline)
        .background(Color("Background"))
    }
}
