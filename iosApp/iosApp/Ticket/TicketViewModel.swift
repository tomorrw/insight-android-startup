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
import Firebase

class TicketViewModel: ObservableObject {
    @Published var pageData: MyQrPresentationModel = MyQrPresentationModel()
    @Published var errorMessage: String? = ""
    private var user: User? = nil
    
    init() {
        self.getData()
    }
    
    @MainActor func getUser() async {
        do {
            let result = asyncSequence(for: GetUserUseCase().getUser())
            
            for try await userResult in result {
                userResult.notificationTopics.forEach{ Messaging.messaging().subscribe(toTopic: $0) }
                self.user = userResult
                self.pageData.load(user: userResult)
            }
        } catch {
            self.errorMessage = "User Not Found!"
            print(error)
        }
    }
    
    @MainActor func getTicketData() async {
        do {
            let result = asyncSequence(for: GetConfigurationUseCase().getTicketInfo())
            
            for try await data in result {
                if data.showTicket {
                    self.pageData = TicketPresentationModel(
                        user: user,
                        rightTitle: data.subTitle,
                        leftTitle: data.title,
                        subText: data.getFormattedDate()?.map{ String($0) },
                        ticketStatus: data.status,
                        description: data.description_
                    )
                } else {
                    self.pageData = EmptyTicketPresentationModel(user: user, description: data.description_)
                }
            }
        } catch {
            self.errorMessage = "Ticket Data Not Found!"
            print(error)
        }
    }
    
    func getData(){
        DispatchQueue.main.async {
            Task{
                await self.getUser()
                await self.getTicketData()
            }
        }
    }
}
