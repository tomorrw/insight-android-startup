//
//  SessionsByCompaniesViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/28/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation

class SessionsByCompaniesViewModel: SessionPageVideoModel {
    @MainActor override func refresh() async {
        isLoading = true
        do {
            self.errorMessage = nil
            let sessionsSequence = asyncSequence(for: GetSessionsUseCase().refresh())
            for try await sessions in sessionsSequence {
                self.allSessions = sessions
                guard let displayedDay = GetAppropriateDisplayedDayForEvent().getDay(events: sessions) else {
                    return
                }
                changeDate(displayedDay)
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor override func getData() async {
        isLoading = true
        do {
            self.errorMessage = nil
            let sessionsSequence = asyncSequence(for: GetSessionsUseCase().getSessions())
            for try await sessions in sessionsSequence {
                self.allSessions = sessions
                guard let displayedDay = GetAppropriateDisplayedDayForEvent().getDay(events: sessions) else {
                    return
                }
                changeDate(displayedDay)
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}
