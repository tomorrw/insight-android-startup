//
//  SectionDisplayPage.swift
//  iosApp
//
//  Created by marcjalkh on 09/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct SectionDisplayPage: View{
    var section: SectionDisplayInfo
    
    var body: some View{
        VStack(alignment: .leading) {
            let info = section.getInfo()
            
            VStack {
                if let image = (info as? InfoContent)?.imageUrl {
                    UrlImageView(urlString: image)
                        .frame(maxWidth: .infinity)
                        .aspectRatio(contentMode: .fit)
                        .clipShape(RoundedRectangle(cornerRadius: 16))
                        .padding(.bottom, 4)
                }
            }
            
            switch(section) {
            case .sessions(_): EmptyView()
            default:
                Text(info.title)
                    .font(.system(size: 18, weight: .medium))
                    .padding(.bottom, 2)
            }
            Text(info.description)
                .foregroundColor(Color("Secondary"))
                .font(.system(size: 15))
                .lineSpacing(3)
            
            switch(section) {
            case let .video(details): if let url = URL(string: details.videoUrl) {
                CustomVideoPlayer(url: url)
                    .aspectRatio(16/9, contentMode: .fit)
                    .background(.gray.opacity(0.3))
                    .frame(maxWidth: .infinity)
                    .clipShape(RoundedRectangle(cornerRadius: 16))
                    .padding(.vertical, 2)
            } else { EmptyView() }
            case let .sessions(details): SessionsVerticalView(sessions: details.sessions)
            case let .speakers(details): SpeakersHorizontalView(speakers: details.speakers)
            default : EmptyView()
            }
            Spacer()
            
        }
        
    }
}
