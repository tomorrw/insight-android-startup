//
//  PostDetailPage.swift
//  iosApp
//
//  Created by Yammine on 4/28/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import DetailPage
import SwiftUI

struct PostDetailPage: View {
    @StateObject private var vm: PostDetailPageViewModel
    
    init(id: String) {
        _vm = StateObject(wrappedValue: PostDetailPageViewModel(id: id))
    }
    
    var body: some View {
        DetailPage(
            vm: vm,
            customHeader: { Text(vm.date).foregroundColor(Color("Secondary")) },
            customBody: {
                if vm.pages.first != nil {
                    VerticalSectionsView(
                        sections: $vm.pages.first!.sections,
                        SectionDisplayView: { SectionDisplayViewImplementation(section: $0) },
                        customFooter: { ActionButtons(actions: vm.action) }
                    )
                }
            }
        )
        .navigationBarTitleDisplayMode(.inline)
        .background(Color("Background"))
    }
}
