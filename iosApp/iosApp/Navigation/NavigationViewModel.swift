//
//  NavigationViewModel.swift
//  iosApp
//
//  Created by Said on 22/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver

@available(iOS 16, *)
class NavigationViewModelNew: ObservableObject {
    @Published var path = NavigationPath()
    
    func popToRoot() {
        path.removeLast(path.count)
    }
    
    func goTo(_ route: Route) {
        path.append(route)
    }
}

@available(iOS 16, *)
struct NavMethodsNew {
    @InjectedObject var navVM: NavigationViewModelNew
    
    static let shared = NavMethodsNew()
    
    func popToRoot() {
        navVM.popToRoot()
    }
    
    func goTo(_ route: Route) {
        navVM.goTo(route)
    }
}
