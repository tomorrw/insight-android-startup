//
//  TicketViewModel.swift
//  iosApp
//
//  Created by marcjalkh on 28/12/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import Combine
import KMPNativeCoroutinesAsync

class TicketViewModel: ObservableObject {
    @Published var showTicket: Bool = true
    @Published var hasDate: Bool = false
    @Published var name: String = "Convenire"
    @Published var description: String = "Your Digital Identity"
    @Published var subText: String? = nil
    @Published var date: [String]? = nil
    @Published var showExhibitionMap: Bool = false
    @Published var showOffers: Bool = false
    @Published var ticketStatus: String? = nil
    @Published var user: User? = nil
    @Published var errorMessage: String? = ""

    private var startDate: Date? = nil
    private var endDate: Date? = nil
    
    init() {
        DispatchQueue.main.async {
            Task{ await self.getUser()
             await self.getTicketData() }
        }
    }
    
    @MainActor func getUser() async {
        Task {
            do {
                let result = asyncSequence(for: GetUserUseCase().getUser())
                
                for try await userResult in result {
                    self.user = userResult
                }
                
            } catch {
                self.errorMessage = "User Not Found!"
                print(error)
            }
        }
    }
    
    @MainActor func getTicketData() async {
        do {
            let result = asyncSequence(for: GetConfigurationUseCase().getTicketInfo())
            
            for try await data in result {
                self.showTicket = data.showTicket
                self.startDate = data.startDate?.description().formatDate("yyyy-MM-dd")
                self.endDate = data.endDate?.description().formatDate("yyyy-MM-dd")
                self.name = data.title
                self.description = data.description_
                self.hasDate = self.startDate != nil && self.endDate != nil
                self.subText = data.subTitle
                self.showOffers = data.showExhibitionOffers
                self.showExhibitionMap = data.showExhibitionMap
                self.ticketStatus = data.status
                self.date = data.getFormattedDate()?.map{ String($0) }
            }
            
        } catch {
            self.errorMessage = "Ticket Data Not Found!"
            print(error)
        }
    }
    
    

    
}
