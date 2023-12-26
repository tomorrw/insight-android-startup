//
//  CompaniesByMapPageViewModel.swift
//  iosApp
//
//  Created by marcjalkh on 25/09/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

class CompanyByMapPageViewModel: ObservableObject{
    @Published var data: [Company] = []
    @Published var isLoading = false
    @Published var errorMessage: String? = nil
    
    @MainActor func getCompanies() async {
        self.isLoading = true
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetCompaniesUseCase().getCompanies())
            for try await companies in sequence {
                self.isLoading = false
                data = companies.filter({ Company in
                    Company.floorMapGroup != nil
                })
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor func refreshData() async {
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetCompaniesUseCase().refresh())
            for try await companies in sequence {
                data = companies.filter { company in
                    company.floorMapGroup != nil
                }
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}
