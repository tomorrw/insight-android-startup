//
//  SocialLinksDisplay.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct SocialLinksDisplay: View {
    var socialLinks: [SocialLink]
    private let uselessUrl = URL(string: "https://google.com")!
    
    init(socialLinks: [SocialLink]) {
        self.socialLinks = Array(socialLinks.filter{ $0.getUrl() != nil}.prefix(5))
    }
    
    var body: some View {
        ZStack {
            if socialLinks.isEmpty == false {
                HStack {
                    Spacer()

                    ForEach(socialLinks.indices, id: \.self) { i in
                        let link = socialLinks[i]
                        
                        VStack {
                            Link(destination: link.getUrl() ?? uselessUrl) {
                                Group {
                                    switch(link) {
                                    case .linkedIn(_): Image(uiImage: UIImage(named: "linkedin")!)
                                    case .facebook(_): Image(uiImage: UIImage(named: "facebook")!)
                                    case .twitter(_): Image(uiImage: UIImage(named: "twitter")!)
                                    case .instagram(_): Image(uiImage: UIImage(named: "instagram")!)
                                    case .website(_): Image(systemName: "link").frame(width: 24)
                                    case .whatsapp(_): Image(uiImage: UIImage(named: "whatsapp")!).renderingMode(.template).resizable().frame(width: 23, height: 23)
                                    case .phone(_): Image(uiImage: UIImage(named: "phone-social")!).renderingMode(.template).resizable().frame(width: 22, height: 22)
                                    case .youtube(_): Image(uiImage: UIImage(named: "youtube-social")!).renderingMode(.template).resizable().frame(width: 25, height: 20)
                                    case .tiktok(_): Image(uiImage: UIImage(named: "tiktok")!).renderingMode(.template).resizable().frame(width: 22, height: 24)
                                    }
                                }
                            }
                            .disabled(link.getUrl() == nil)
                            .foregroundColor(link.getUrl() == nil ? Color("OnSurface-Secondary") : Color("Primary"))
                        }
                        .frame(width: 26)
                        
                        Spacer()
                    }
                }
            }
        }
    }
}



//struct SocialLinksDisplay_Previews: PreviewProvider {
//    static var previews: some View {
//        SocialLinksDisplay()
//    }
//}
