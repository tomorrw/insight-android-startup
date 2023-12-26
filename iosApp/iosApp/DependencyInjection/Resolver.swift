//
//  Resolver.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Resolver

extension Resolver: ResolverRegistering {
    public static func registerAllServices() {
        register {
            AuthenticationViewModel()
        }
        .scope(.application)
        
        register {
            SpeakersViewModel()
        }
        .scope(.application)
        
        register {
            CompaniesPageViewModel()
        }
        .scope(.application)
        
        register {
            HomePageViewModel()
        }
        .scope(.application)
        
        if #available(iOS 16, *) {
            register {
                NavigationViewModelNew()
            }
            .scope(.application)
        }
        
        register {
            DeepLinkingViewModel()
        }
        .scope(.application)
        
        register {
            CompanyByMapPageViewModel()
        }
        .scope(.application)
        
    }
}
