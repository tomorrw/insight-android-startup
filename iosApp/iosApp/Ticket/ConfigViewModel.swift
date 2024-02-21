//
//  ConfigViewModel.swift
//  iosApp
//
//  Created by marcjalkh on 08/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import Combine
import KMPNativeCoroutinesAsync

class ConfigViewModel: ObservableObject{
    @Published var showExhibitionMap: Bool = false
    @Published var showOffers: Bool = false

    init(){
        DispatchQueue.main.async {
            Task{ await self.getConfigData() }
        }
    }
    
    @MainActor func getConfigData() async {
        do {
            let result = asyncSequence(for: GetConfigurationUseCase().getTicketInfo())
            
            for try await data in result {
                self.showOffers = data.showExhibitionOffers
                self.showExhibitionMap = data.showExhibitionMap
            }
            
        } catch {
            print(error)
        }
    }
}
