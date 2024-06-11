//
//  SocialLinksMapper.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared

extension Array where Element == SharedSocialLink {
    func mapToSocialLinkUI() -> [SocialLink] {
        SocialLink.allCases.map { emptyLink in
           let link = self.first { link in
               switch(emptyLink) {
               case .linkedIn(_): return link.platform == SharedSocialLink.Platform.linkedin
               case .twitter(_): return link.platform == SharedSocialLink.Platform.twitter
               case .website(_): return link.platform == SharedSocialLink.Platform.website
               case .instagram(_): return link.platform == SharedSocialLink.Platform.instagram
               case .facebook(_): return link.platform == SharedSocialLink.Platform.facebook
               case .whatsapp(_): return link.platform == SharedSocialLink.Platform.whatsapp
               case .phone(_): return link.platform == SharedSocialLink.Platform.phone
               case .youtube(_): return link.platform == SharedSocialLink.Platform.youtube
               case .tiktok(_): return link.platform == SharedSocialLink.Platform.tiktok
               }
           }
           
           return emptyLink.changeUrl(URL(string: link?.url ?? ""))
       }
    }
}
