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
    var pageData: MyQrPresentationModel = MyQrPresentationModel()
    @Published var errorMessage: String? = ""
    
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
                    self.pageData.user = userResult
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
                if data.showTicket {
                    self.pageData = TicketPresentationModel(
                        rightTitle: data.subTitle,
                        leftTitle: data.title,
                        subText: data.getFormattedDate()?.map{ String($0) },
                        ticketStatus: data.status
                    )
                }
                self.pageData.description = data.description_
            }
            
        } catch {
            self.errorMessage = "Ticket Data Not Found!"
            print(error)
        }
    }
    
}

class MyQrPresentationModel: ObservableObject{
    @Published var user: User? = nil
    @Published var description: String = "Your Digital Identity"
}

class TicketPresentationModel: MyQrPresentationModel{
    @Published var rightTitle: String? = nil
    @Published var leftTitle: String = "Convenire"
    @Published var subText: [String]? = nil
    @Published var ticketStatus: String? = nil
    
    init(rightTitle: String? = nil, leftTitle: String, subText: [String]? = nil, ticketStatus: String? = nil) {
        self.rightTitle = rightTitle
        self.leftTitle = leftTitle
        self.subText = subText
        self.ticketStatus = ticketStatus
    }
}
