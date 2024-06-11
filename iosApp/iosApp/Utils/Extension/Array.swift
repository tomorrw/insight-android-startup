//
//  Array.swift
//  iosApp
//
//  Created by Said on 22/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation

extension Array<SocialLink> {
    func ensureSize(size: Int) -> [SocialLink] {
        if self.count == size { return self }
        
        if self.count < size {
            var socialLinkPool = SocialLink.allCases.filter { platform in
                self.allSatisfy { $0 != platform }
            }
            
            return self + (1...(size - self.count)).map {  _ in
                if socialLinkPool.isEmpty {
                    socialLinkPool = SocialLink.allCases
                }
                let link = socialLinkPool.randomElement()!
                socialLinkPool.remove(at: socialLinkPool.firstIndex(of: link)!)
                
                return link.changeUrl(nil)
            }
        }
        
        let newList = self.filter { $0.getUrl() != nil }
        
        if newList.count > 5 {
            return Array(newList.prefix(upTo: 6))
        } else {
            return newList.ensureSize(size: 5)
        }
    }
}
