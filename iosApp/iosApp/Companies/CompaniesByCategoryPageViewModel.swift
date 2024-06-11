//
//  CompaniesByCategoryPageViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

class CompaniesByCategoryPageViewModel: SearchViewModelImplementation {
    private let categoryId: String
    
    init(categoryId: String) {
        self.categoryId = categoryId
        super.init()
        Task { await getCompanies() }
    }
    
    @MainActor override func refreshData() async {
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetCompaniesByCategoryUseCase().refresh(categoryId: categoryId))
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
            let sequence = asyncSequence(for: GetCompaniesByCategoryUseCase().getCompanies(categoryId: categoryId))
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
