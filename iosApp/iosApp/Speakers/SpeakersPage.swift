//
//  SpeakersPage.swift
//  iosApp
//
//  Created by Yammine on 4/25/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver
struct SpeakersPage: View {
    @InjectedObject private var vm: SpeakersViewModel
    
    var body: some View {
        SearchableList(vm: vm, searchPlaceholder: "Countries are sorted alphabetically") { item in
            SpeakerDetailPage(id: item.id)
        }
        .task { await vm.getSpeakers() } 
        .navigationTitle("Speakers")
        .navigationBarTitleDisplayMode(.inline)
        .background(Color("Background"))
    }
}
