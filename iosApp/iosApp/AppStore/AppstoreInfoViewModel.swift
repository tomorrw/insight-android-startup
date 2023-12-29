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
    
    func fetchAppStoreUrl(){
        //if bundle id is found in app
        guard let bundleId = Bundle.main.infoDictionary?[kCFBundleIdentifierKey as String] as? String,
              let urlEncodedBundleId = bundleId.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed),
              let lookupUrl = URL(string: "http://itunes.apple.com/lookup?bundleId=\(urlEncodedBundleId)")
        else {
            self.url = self.defaultUrl
            self.appName = self.defaultName
            return
        }
        //setup session
        let session = URLSession(configuration: .default)
        let downloadTask = session.dataTask(with: URLRequest(url: lookupUrl), completionHandler: { (data, response, error) -> Void in
            //update found info
            DispatchQueue.main.async() {
                if error == nil,
                   let data = data,
                   let json = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
                   let results = json["results"] as? [[String: Any]],
                   !results.isEmpty,
                   let trackViewUrl = results.first?["trackViewUrl"] as? String,
                   let appName = results.first?["trackName"] as? String {
                        self.url = URL(string: trackViewUrl)!
                        self.appName = String(appName)
                } else {
                    self.url = self.defaultUrl
                    self.appName = self.defaultName
                }
            }
            
        })
        downloadTask.resume()
    }
}
