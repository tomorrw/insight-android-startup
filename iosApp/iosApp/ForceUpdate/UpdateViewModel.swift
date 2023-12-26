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
    
    init() {
        DispatchQueue.main.async {
            self.getUpdateUseCase()
        }
    }
}
