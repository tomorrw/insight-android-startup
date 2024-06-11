//
//  SpeakersHorizontalView.swift
//  iosApp
//
//  Created by Yammine on 4/26/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import ImageCaching
import UiComponents

public struct SpeakersHorizontalView: View {
    let speakers: [EntityModel]
    
    public init(_ speakers: [EntityModel]) {
        self.speakers = speakers
    }
    
    public var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            LazyHStack(alignment: .top) {
                ForEach(speakers) { speaker in
                    NavigateTo {
                        SpeakerDetailPage(id: speaker.id)
                    } label: {
                        EntityCard(entity: speaker)
                    }
                }
            }
        }
    }
}
