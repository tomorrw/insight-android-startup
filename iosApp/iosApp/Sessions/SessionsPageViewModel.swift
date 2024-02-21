//
//  SessionsPageViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

struct DateDisplayInfo: Identifiable {
    let id: UUID = UUID()
    let isEnabled: Bool
    let date: Kotlinx_datetimeLocalDate
}

private let defaultLocationFilter = "All Locations"

class SessionsPageViewModel: ObservableObject {
    @Published var datesDisplayed: [DateDisplayInfo] = []
    @Published var sessionsDisplayed: [Session] = []
    @Published var displayedLocation: [String] = []
    @Published var locationChoice: String = defaultLocationFilter
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil
    private var shouldFilterForCompanies: Bool
    private var currentSessions: [Session] = []
    private var currentDate: Kotlinx_datetimeLocalDate? = nil
    
    private var allSessions: [Session] = []

    init(onlyDisplayCompaniesEvents: Bool = false) {
        self.shouldFilterForCompanies = onlyDisplayCompaniesEvents
        Task { await getData() }
    }
    
    @MainActor func refresh() async {
        do {
            self.errorMessage = nil
            let sessionsSequence = asyncSequence(for: GetSessionsUseCase().refresh())
            for try await sessions in sessionsSequence {
                self.allSessions = shouldFilterForCompanies ? sessions.filter{ $0.companyId != nil } : sessions
                
                guard let displayedDay = GetAppropriateDisplayedDayForEvent().getDay(events: self.allSessions) else {
                    return
                }
                changeDate(currentDate != nil ? currentDate! : displayedDay)
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor func getData() async {
        isLoading = true
        do {
            self.errorMessage = nil
            let sessionsSequence = asyncSequence(for: GetSessionsUseCase().getSessions())
            for try await sessions in sessionsSequence {
                isLoading = false
                self.allSessions = shouldFilterForCompanies ? sessions.filter{ $0.companyId != nil } : sessions
                
                guard let displayedDay = GetAppropriateDisplayedDayForEvent().getDay(events: self.allSessions) else {
                    return
                }
                changeDate(displayedDay)
            }
        } catch {
            isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    
    func changeDate(_ displayedDay: Kotlinx_datetimeLocalDate) {
        self.currentDate = displayedDay
        self.currentSessions = allSessions.filter { $0.startTime.date == displayedDay }
        self.datesDisplayed = GetDatesFromEventsUseCase().getDates(events: allSessions).map{ date in
            DateDisplayInfo(isEnabled: date == displayedDay, date: date)
        }
        
        self.displayedLocation = [defaultLocationFilter] + self.currentSessions.map { $0.location }.unique.sorted().reversed()
        filterLocation(location: locationChoice)
    }
    
    func filterLocation(location: String){
        self.locationChoice = location
        
        self.sessionsDisplayed = self.currentSessions.filter {
            self.locationChoice == defaultLocationFilter || self.locationChoice == $0.location
        }
    }
    
}
