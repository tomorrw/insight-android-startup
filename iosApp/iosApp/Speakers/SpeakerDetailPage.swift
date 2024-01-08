//
//  SpeakerDetailsView.swift
//  iosApp
//
//  Created by Yammine on 4/26/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct SpeakerDetailPage: View {
    @StateObject private var vm: SpeakerDetailPageViewModel
    
    init(id: String) {
        _vm = StateObject(wrappedValue: SpeakerDetailPageViewModel(id: id))
    }
    
    var body: some View {
        DetailPage(
            vm: vm,
            customHeader: { SocialLinksDisplay(socialLinks: vm.socialLinks) },
            customBody: { PageTabDisplayView(pages: $vm.sections) }
        )
            .navigationBarTitleDisplayMode(.inline)
            .background(Color("Background"))
    }
}
