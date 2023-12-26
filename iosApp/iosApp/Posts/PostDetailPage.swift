//
//  PostDetailPage.swift
//  iosApp
//
//  Created by Yammine on 4/28/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation

import SwiftUI

struct PostDetailPage: View {
    @StateObject private var vm: PostDetailPageViewModel
    
    init(id: String) {
        _vm = StateObject(wrappedValue: PostDetailPageViewModel(id: id))
    }
    
    var body: some View {
        DetailPage(vm: vm)
            .navigationBarTitleDisplayMode(.inline)
            .background(Color("Background"))
    }
}
