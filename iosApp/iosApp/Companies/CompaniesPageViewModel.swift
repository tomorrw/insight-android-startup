//
//  CompaniesViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

class CompaniesPageViewModel: SearchViewModelImplementation {
    init() {
        super.init()
        Task { await getCompanies() }
    }
    
    @MainActor override func refreshData() async {
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetCompaniesUseCase().refresh())
            for try await companies in sequence {
                self.changeOriginalList(companies.mapToSearchItems())
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor func getCompanies() async {
        self.isLoading = true
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetCompaniesUseCase().getCompanies())
            for try await companies in sequence {
                self.isLoading = false
                self.changeOriginalList(companies.mapToSearchItems())
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}
