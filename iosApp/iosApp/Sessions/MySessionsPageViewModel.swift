//
//  MySessionsPageViewModel.swift
//  iosApp
//
//  Created by Said on 16/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import KMPNativeCoroutinesAsync

private let defaultLocationFilter = "All Locations"

class MySessionsPageViewModel: ObservableObject {
    @Published var datesDisplayed: [DateDisplayInfo] = []
    @Published var sessionsDisplayed: [Session] = []
    @Published var displayedLocation: [String] = []
    @Published var locationChoice: String = defaultLocationFilter
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil
    private var currentSessions: [Session] = []
    var currentDate: Kotlinx_datetimeLocalDate? = nil
    
    private var allSessions: [Session] = []
    private var mySessions: [Session] = [] {
        didSet {
            DispatchQueue.main.async {
                withAnimation {
                    self.currentSessions = self.mySessions.filter {
                        $0.startTime.date == self.datesDisplayed.first(where: {
                            $0.isEnabled
                        })?.date ?? GetAppropriateDisplayedDayForEvent().getDay(events: self.mySessions)
                    }
                    
                    
                    self.displayedLocation = [defaultLocationFilter] + self.currentSessions.map { $0.location }.unique.sorted().reversed()
                    
                    if (!self.displayedLocation.contains(self.locationChoice)) {
                        self.locationChoice = self.displayedLocation.first ?? defaultLocationFilter
                    }
                    
                    self.sessionsDisplayed = self.currentSessions.filter {
                        self.locationChoice == defaultLocationFilter || self.locationChoice == $0.location
                    }
                }
            }
        }
    }
    private var shouldFilterForCompanies: Bool
    
    init(onlyDisplayCompaniesEvents: Bool = false) {
        self.shouldFilterForCompanies = onlyDisplayCompaniesEvents
        Task { await getData() }
    }
    
    @MainActor func refresh(keepCurrentDate: Bool = true) async {
        do {
            self.errorMessage = nil
            let sessionsSequence = asyncSequence(for: GetSessionsUseCase().refresh())
            for try await sessions in sessionsSequence {
                self.allSessions = (shouldFilterForCompanies ? sessions.filter{ $0.companyId != nil } : sessions)
                
                self.mySessions = self.allSessions.filter { session in
                    ShouldNotifyEventUseCase().shouldNotify(id: session.id)
                }
                
                guard let displayedDay = GetAppropriateDisplayedDayForEvent().getDay(events: self.mySessions) else {
                    return
                }
                
                keepCurrentDate ? changeDate(currentDate ?? displayedDay) : changeDate(displayedDay)
                
                await watchForBookmarked(sessionIds: mySessions.map { $0.id })
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
                self.allSessions = (shouldFilterForCompanies ? sessions.filter{ $0.companyId != nil } : sessions)
                
                self.mySessions = self.allSessions.filter { session in
                    ShouldNotifyEventUseCase().shouldNotify(id: session.id)
                }
                
                guard let displayedDay = GetAppropriateDisplayedDayForEvent().getDay(events: self.mySessions) else {
                    return
                }
                changeDate(displayedDay)
                
                await watchForBookmarked(sessionIds: mySessions.map { $0.id })
            }
        } catch {
            isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    func watchForBookmarked(sessionIds: [String]) async {
        do {
            let result = asyncSequence(for: ShouldNotifyEventUseCase().shouldNotifyFlow(listIds: sessionIds))
            
            for try await bookmarkedSessions in result {
                mySessions = mySessions.filter { bookmarkedSessions[$0.id] as? Bool ?? false }
            }
        } catch {
            print(error)
        }
    }
    
    func changeDate(_ displayedDay: Kotlinx_datetimeLocalDate) {
        self.currentDate = displayedDay
        
        self.currentSessions = mySessions.filter { $0.startTime.date == displayedDay }
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
