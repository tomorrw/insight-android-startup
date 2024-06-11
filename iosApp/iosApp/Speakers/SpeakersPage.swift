//
//  SpeakersPage.swift
//  iosApp
//
//  Created by Yammine on 4/25/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver
import UiComponents
import SearchableList

struct SpeakersPage: View {
    @InjectedObject private var vm: SpeakersViewModel
    
    var body: some View {
        SearchableList(
            vm: vm,
            searchPlaceholder: "Countries are sorted alphabetically",
            rowView: { item in
                NavigateTo {
                    SpeakerDetailPage(id: item.id)
                } label: {
                    DefaultListItem(item: item)
                }
                
            }
        )
        .task { await vm.getSpeakers() }
        .navigationTitle("Speakers")
        .navigationBarTitleDisplayMode(.inline)
        .background(Color("Background"))
    }
}
