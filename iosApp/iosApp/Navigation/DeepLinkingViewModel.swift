//
//  DeepLinkingViewModel.swift
//  iosApp
//
//  Created by Said on 22/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

///
/// URL Format:
/// usjfmd://host/pathComponents[1]/pathComponents[2]...
/// pathComponents[0] is the separator ' / '
///
/// company: https://host/company/{id}
/// event: https://host/event/{id}
/// post: https://host/post/{id}
/// speaker: https://host/speaker/{id}
///
class DeepLinkingViewModel: ObservableObject {
    @Environment(\.openURL) private var openURL
    @Published var companyId: String? = nil
    @Published var eventId: String? = nil
    @Published var postId: String? = nil
    @Published var speakerId: String? = nil
    
    @Published var viewCompanyPage: Bool = false
    @Published var viewEventPage: Bool = false
    @Published var viewPostPage: Bool = false
    @Published var viewSpeakerPage: Bool = false
    @Published var viewProgressPage: Bool = false
    
    func checkDeepLink(_ string: String) {
        checkDeepLink(url: URL(string: string))
    }
    
    func checkDeepLink(url: URL?) {
        guard let url = url else {
            return
        }
        
        let resourceType = url.pathComponents[safe: 1] ?? ""
        let resourceId = url.pathComponents[safe: 2] ?? ""
        
        if resourceType == "company" {
            linkToCompany(resourceId)
        } else if resourceType == "event" {
            linkToEvent(resourceId)
        } else if resourceType == "post" {
            linkToPost(resourceId)
        } else if resourceType == "speaker" {
            linkToSpeaker(resourceId)
        } else if resourceType == "progress" {
            linkToProgress()
        } else {
            openURL(url)
        }
    }
    
    func checkNotification(payload: [AnyHashable: Any]) {
        guard
            let urlString = payload[AnyHashable("deeplink")] as? String,
            let url = URL(string: urlString) else { return }
        
        checkDeepLink(url: url)
    }
    
    func linkToCompany(_ id: String) {
        if #available(iOS 16, *) {
            NavMethodsNew.shared.goTo(.company(id: id))
        } else {
            companyId = id
            viewCompanyPage = true
        }
    }
    
    func linkToEvent(_ id: String) {
        if #available(iOS 16, *) {
            NavMethodsNew.shared.goTo(.event(id: id))
        } else {
            eventId = id
            viewEventPage = true
        }
    }
    
    func linkToPost(_ id: String) {
        if #available(iOS 16, *) {
            NavMethodsNew.shared.goTo(.post(id: id))
        } else {
            postId = id
            viewPostPage = true
        }
    }
    
    func linkToSpeaker(_ id: String) {
        if #available(iOS 16, *) {
            NavMethodsNew.shared.goTo(.speaker(id: id))
        } else {
            speakerId = id
            viewSpeakerPage = true
        }
    }
    
    func linkToProgress() {
        if #available(iOS 16, *) {
            NavMethodsNew.shared.goTo(.progress)
        } else {
            viewProgressPage = true
        }
    }
}
