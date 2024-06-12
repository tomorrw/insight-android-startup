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
//    case company(id: String)
//    case progress
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
                    
//                    if let companyId = deepLinkingViewModel.companyId {
//                        NavigateToWithActive(
//                            destination: { CompanyPage(id: companyId) },
//                            label: { EmptyView() },
//                            isActive: $deepLinkingViewModel.viewCompanyPage
//                        )
//                    }
//                    NavigateToWithActive(
//                        destination: { MyProgressPage() },
//                        label: { EmptyView() },
//                        isActive: $deepLinkingViewModel.viewProgressPage
//                    )
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
                    default: HomePage()
//                    case .company(id: let id): CompanyPage(id: id)
//                    case .progress: MyProgressPage()
                    }
                }
        }
    }
}
