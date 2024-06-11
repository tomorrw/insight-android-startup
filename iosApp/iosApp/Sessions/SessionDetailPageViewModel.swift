//
//  SessionDetailPageViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync
import DetailPage
import SwiftUI
import UiComponents
class SessionDetailPageViewModel: DetailPageViewModel {
    let id: String
    var subjectId: String = ""
    @Published var timeInterval: String = ""
    @Published var location: String = ""
    @Published var date: String = ""
    @Published var canAskQuestions: Bool = false
    @Published var attendees: String? = nil
    @Published var action: [Action] = []
    @Published var tag: Tag? = nil
    @Published var minutesAttended: Int? = nil
    @Published var speakers: [EntityModel] = []
    
    init(id: String) {
        self.id = id
        super.init()
        Task { await getData() }
    }
    
    @MainActor func getData() async {
        isLoading = true
        do {
            self.errorMessage = nil
            let sessionSequence = asyncSequence(for: GetSessionByIdUseCase().getSession(id: id))
            for try await data in sessionSequence {
                self.subjectId = self.id
                self.isLoading = false
                self.title = data.title
                self.description = data.topic
                self.location = data.location
                self.headerDesign = .detailPage
                self.image = data.image
                self.pages = [data.detailPage.getDataIfLoaded()].compactMap{ $0 }.mapToPagePresentationModel()
                self.timeInterval = data.getTimeInterval()
                self.canAskQuestions = data.canAskQuestions
                if let tag = data.getTag() { self.tag = tag.toPresentationModel() }
                if let minutes = data.minutesAttended { self.minutesAttended = Int(truncating: minutes) }
                self.action = data.actions
                self.attendees = data.getAttendeesCount()
                self.speakers = data.speakers.toEntitiesPresentationModel()
                
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd"
                if let date = dateFormatter.date(from: data.getDateString()) {
                    dateFormatter.dateFormat = "MMM d, yyyy"
                    self.date = dateFormatter.string(from: date)
                }
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}
