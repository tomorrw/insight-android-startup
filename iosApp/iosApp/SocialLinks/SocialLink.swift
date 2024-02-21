//
//  SocialLink.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation

enum SocialLink: Hashable, CaseIterable {
    static var allCases: [SocialLink] = [
        .linkedIn(nil),
        .twitter(nil),
        .website(nil),
        .instagram(nil),
        .facebook(nil),
        .whatsapp(nil),
        .phone(nil),
        .youtube(nil),
        .tiktok(nil)
    ]
    
    func changeUrl(_ url: URL? = nil) -> SocialLink {
        switch self {
        case .linkedIn(_): return .linkedIn(url)
        case .twitter(_): return .twitter(url)
        case .website(_): return .website(url)
        case .instagram(_): return .instagram(url)
        case .facebook(_): return .facebook(url)
        case .whatsapp(_): return .whatsapp(url)
        case .phone(_): return .phone(url)
        case .youtube(_): return .youtube(url)
        case .tiktok(_): return .tiktok(url)
        }
    }
    
    func getUrl() -> URL?  {
        switch self {
        case let .linkedIn(url): return url
        case let .twitter(url): return url
        case let .website(url): return url
        case let .instagram(url): return url
        case let .facebook(url): return url
        case let .whatsapp(url): return url
        case let .phone(url): return url
        case let .youtube(url): return url
        case let .tiktok(url): return url
        }
    }
    
    case linkedIn(URL?)
    case twitter(URL?)
    case website(URL?)
    case instagram(URL?)
    case facebook(URL?)
    case whatsapp(URL?)
    case phone(URL?)
    case youtube(URL?)
    case tiktok(URL?)
}
