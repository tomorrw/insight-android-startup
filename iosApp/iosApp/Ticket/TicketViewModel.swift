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
    private var usecase = shared.LiveNotificationListenerUseCase()
    @Published var websocketMessage: String = ""
    
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
    func listenToMessage() {
        
        guard let res = try? self.usecase.getMessageIOS() else{
            return
        }
        
        res.fold(
            onSuccess: { [weak self] message in
                guard let self = self,
                      let msg = message as? String  else {
                    return
                }
                DispatchQueue.main.async {
                    self.websocketMessage = msg
                    self.vibrate()
                }
            },
            onFailure: { [weak self] err in
                guard let self = self else { return }
                self.websocketMessage = err.toUserFriendlyError()
            }
        )
        
    }
    
    func vibrate() {
        let impactMed = UIImpactFeedbackGenerator(style: .medium)
        impactMed.impactOccurred()
    }
    
    @MainActor func startListening(){
        self.usecase.startListening(callback: self.listenToMessage)
    }
    
    @MainActor func stopListening(){
        self.usecase.stopListening()
    }

    
}
