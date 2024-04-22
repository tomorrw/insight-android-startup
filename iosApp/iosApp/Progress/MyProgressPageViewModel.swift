//
//  MyProgressPageViewModel.swift
//  iosApp
//
//  Created by Yammine on 22/04/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import Foundation
import SwiftUI
import KMPNativeCoroutinesAsync
import shared

class MyProgressPageViewModel: ObservableObject {
    @Published var data: shared.ProgressReport? = nil
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil
    
    init(onlyDisplayCompaniesEvents: Bool = false) {
        Task { await getData() }
    }
    
    @MainActor func refresh() async {
        do {
            self.errorMessage = nil
            let progressReportSequence = asyncSequence(for: GetProgressReportUseCase().refresh())
            for try await progressReport in progressReportSequence {
                data = progressReport
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor func getData() async {
        isLoading = true
        do {
            self.errorMessage = nil
            let progressReportSequence = asyncSequence(for: GetProgressReportUseCase().getMyProgress())
            for try await progressReport in progressReportSequence {
                isLoading = false
                data = progressReport
            }
        } catch {
            isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}

