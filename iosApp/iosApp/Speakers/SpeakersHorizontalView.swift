//
//  SpeakersHorizontalView.swift
//  iosApp
//
//  Created by Yammine on 4/26/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared

struct SpeakersHorizontalView: View {
    let speakers: [Speaker]
    
    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            LazyHStack(alignment: .top) {
                ForEach(speakers) { speaker in
                    NavigateTo {
                        SpeakerDetailPage(id: speaker.id)
                    } label: {
                        VStack {
                            ZStack(alignment: .bottomTrailing) {
                                UrlImageView(urlString: speaker.image)
                                    .scaledToFill()
                                    .frame(width: 72, height: 72)
                                    .clipShape(Circle())
                                if speaker.nationality != nil {
                                    UrlImageView(urlString: speaker.nationality?.url)
                                        .clipShape(Circle())
                                        .frame(width: 23, height: 23)
                                }
                            }
                            .frame(alignment: .bottomTrailing)
                            
                            Text(speaker.fullName.getFormattedName())
                                .multilineTextAlignment(.center)
                                .lineLimit(2)
                                .frame(height: 40, alignment: .top)
                            
                        }
                        .frame(width: 120)
                    }
                    .background(Color("Background"))
                }
            }
        }
    }
}

struct SpeakersHorizontalView_Previews: PreviewProvider {
    static var previews: some View {
        SpeakersHorizontalView(speakers: [])
    }
}
