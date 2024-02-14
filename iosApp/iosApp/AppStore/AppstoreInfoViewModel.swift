//
//  AppstoreInfoViewModel.swift
//  iosApp
//
//  Created by marcjalkh on 29/12/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared

class AppstoreInfoViewModel: ObservableObject {
    let defaultName = "App"
    let defaultUrl: URL = URL(string: "https://convenire.tomorrow.services/")!
    
    @Published var url: URL
    @Published var appName: String
    
    init(){
        self.url = self.defaultUrl
        self.appName = self.defaultName
        DispatchQueue.main.async { self.fetchAppStoreUrl() }
    }
    
    func fetchAppStoreUrl()  {
        let result = GetAppConfigUseCase().get()
        self.appName = result.name
        guard let url = result.updateUrl else { return }
        self.url = URL(string: url) ?? self.defaultUrl
    }
}
