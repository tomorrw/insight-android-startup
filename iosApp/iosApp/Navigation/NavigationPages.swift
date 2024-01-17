//
//  NavigationPages.swift
//  iosApp
//
//  Created by Said on 22/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver

enum Route: Hashable {
    case company(id: String)
    case event(id: String)
    case post(id: String)
    case speaker(id: String)
}

struct NavigationPages<Content: View>: View {
    @InjectedObject var deepLinkingViewModel: DeepLinkingViewModel
    
    @ViewBuilder let content: Content
    
    var body: some View {
        if #available(iOS 16, *) {
            NavigationStackNew {
                content
                    .navigationBarHidden(true)
            }
        } else {
            NavigationView {
                ZStack {
                    content
                        .navigationBarHidden(true)
                    
                    if let companyId = deepLinkingViewModel.companyId {
                        NavigateToWithActive(
                            destination: { CompanyPage(id: companyId) },
                            label: { EmptyView() },
                            isActive: $deepLinkingViewModel.viewCompanyPage
                        )
                    }
                    if let eventId = deepLinkingViewModel.eventId {
                        NavigateToWithActive(
                            destination: { SessionDetailPage(id: eventId) },
                            label: { EmptyView() },
                            isActive: $deepLinkingViewModel.viewEventPage
                        )
                    }
                    if let postId = deepLinkingViewModel.postId {
                        NavigateToWithActive(
                            destination: { PostDetailPage(id: postId) },
                            label: { EmptyView() },
                            isActive: $deepLinkingViewModel.viewPostPage
                        )
                    }
                    if let speakerId = deepLinkingViewModel.speakerId {
                        NavigateToWithActive(
                            destination: { SpeakerDetailPage(id: speakerId) },
                            label: { EmptyView() },
                            isActive: $deepLinkingViewModel.viewSpeakerPage
                        )
                    }
                }
            }
            .navigationViewStyle(.stack)
        }
    }
}

@available(iOS 16, *)
struct NavigationStackNew<Content: View>: View {
    @InjectedObject var vm: NavigationViewModelNew
    
    @ViewBuilder let content: Content
    
    var body: some View {
        NavigationStack(path: $vm.path) {
            content
                .navigationDestination(for: Route.self) { route in
                    switch route {
                    case .company(id: let id): CompanyPage(id: id)
                    case .event(id: let id): SessionDetailPage(id: id)
                    case .post(id: let id): PostDetailPage(id: id)
                    case .speaker(id: let id): SpeakerDetailPage(id: id)
                    }
                }
        }
    }
}
