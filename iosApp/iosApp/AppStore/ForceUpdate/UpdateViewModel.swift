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

class UpdateViewModel: AppstoreInfoViewModel {
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
                showUpdate()
            } catch {
            }
        }
    }
    
    @MainActor
    func getAppConfig() async {
        
        guard let bundleId = Bundle.main.infoDictionary?[kCFBundleIdentifierKey as String] as? String,
              let res = try? await AppleInfo().getAppleInfo(bundleId: bundleId)
        else{
            print("res return")
            return }
        
        res.fold(
            onSuccess: { [weak self] data in
                guard let self = self else { return }
                guard let info = data as? IOSAppInfo  else {
                    print("conv return")
                    return
                }
                print(info.appName)
                print(info.storeUrl  ?? "no store ")
            },
            onFailure: { [weak self] err in
                guard let self = self else { return }
                print(err)
            }
        )
//        do {
//            let result = try await AppleInfo().getAppleInfo(bundleId: bundleId)
//            print(result?.appName)
//            print(result?.storeUrl)
//        } catch {
//            print(error)
//        }
    }

    override init() {
        super.init()
        DispatchQueue.main.async {
            Task{ await super.fetchAppStoreUrl()
                await self.getAppConfig()
            }
            self.getUpdateUseCase()
        }
    }
}

