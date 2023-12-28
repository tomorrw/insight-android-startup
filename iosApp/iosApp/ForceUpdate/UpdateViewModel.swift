//
//  UpdateViewModel.swift
//  iosApp
//
//  Created by Said on 01/11/2022.
//  Copyright © 2022 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import KMPNativeCoroutinesAsync

class UpdateViewModel: ObservableObject {
    private let defaultName = "App"
    private let defaultUrl: URL = URL(string: "https://convenire.tomorrow.services/")!
    
    @Published var url: URL = URL(string: "https://tomorrow.services/")!
    @Published var appName = "App"
    @Published var showingPopup: Bool = false
    
    typealias UpdateType = GetUpdateTypeUseCase.UpdateType
    
    var updateType: UpdateType?
    var isCancellable: Bool = true
    
    let getUpdateTypeUseCase = GetUpdateTypeUseCase()
    @MainActor var getUpdateTask: Task<(), Never>? = nil
    
    func showUpdate() {
        switch updateType {
        case UpdateType.forced:
            openPopup(false)
        case UpdateType.flexible:
            openPopup(true)
        default:
            return
        }
    }
    
    func openPopup(_ isCancellable: Bool) {
        self.isCancellable = isCancellable
        showingPopup = true
    }
    
    func closePopup() {
        showingPopup = false
    }
    
    @MainActor
    func getUpdateUseCase() {
        getUpdateTask = Task {
            do {
                let result = try await getUpdateTypeUseCase.getType()
                self.updateType = result
                switch result {
                case .forced:
                    openPopup(false)
                case .flexible:
                    openPopup(true)
                default: return
                }
            } catch {
            }
        }
    }
    
    func fetchAppStoreUrl(completionHandler: @escaping (URL?, String?) -> Void) {
        
        guard let bundleId = Bundle.main.infoDictionary?[kCFBundleIdentifierKey as String] as? String,
            let urlEncodedBundleId = bundleId.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed),
            let lookupUrl = URL(string: "http://itunes.apple.com/lookup?bundleId=\(urlEncodedBundleId)") else {
            completionHandler(nil, nil)
            return
        }
        let session = URLSession(configuration: .default)
        let downloadTask = session.dataTask(with: URLRequest(url: lookupUrl), completionHandler: { (data, response, error) -> Void in
            DispatchQueue.main.async() {
                if error == nil,
                   let data = data,
                   let json = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
                   let results = json["results"] as? [[String: Any]],
                   !results.isEmpty,
                   let trackViewUrl = results.first?["trackViewUrl"] as? String,
                   let appName = results.first?["trackName"] as? String {
                    completionHandler(URL(string: trackViewUrl), String(appName))
                } else {
                    completionHandler(nil, nil)
                }
            }
        })
        downloadTask.resume()
    }
    
    
    
    init() {
        DispatchQueue.main.async {
            self.fetchAppStoreUrl() { appStoreUrl, appName in
                self.url = appStoreUrl ?? self.defaultUrl
                self.appName = appName ?? self.defaultName
                self.getUpdateUseCase()
            }
        }
    }
}

