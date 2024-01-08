//
//  AppstoreInfoViewModel.swift
//  iosApp
//
//  Created by marcjalkh on 29/12/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation

class AppstoreInfoViewModel: ObservableObject {
    let defaultName = "App"
    let defaultUrl: URL = URL(string: "https://convenire.tomorrow.services/")!
    
    @Published var url: URL
    @Published var appName: String
    
    init(){
        self.url = self.defaultUrl
        self.appName = self.defaultName
    }
    
    @MainActor
    func fetchAppStoreUrl() async {
        guard let bundleId = Bundle.main.infoDictionary?[kCFBundleIdentifierKey as String] as? String,
              let urlEncodedBundleId = bundleId.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed),
              let lookupUrl = URL(string: "http://itunes.apple.com/lookup?bundleId=\(urlEncodedBundleId)"),
              let response = try? await URLSession(configuration: .default).data(from: lookupUrl),
              let json = try? JSONSerialization.jsonObject(with: response.0) as? [String: Any],
              let results = json["results"] as? [[String: Any]], !results.isEmpty,
              let trackViewUrl = results.first?["trackViewUrl"] as? String,
              let appName = results.first?["trackName"] as? String
        else { return }
        
        self.url = URL(string: trackViewUrl)!
        self.appName = String(appName)
        
    }
}
