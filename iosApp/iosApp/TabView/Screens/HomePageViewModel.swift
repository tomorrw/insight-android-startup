//
//  HomePageViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/28/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//
import shared
import KMPNativeCoroutinesAsync

class HomePageViewModel: ObservableObject {
    @Published var data: HomeData? = nil
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil
    
    init(onlyDisplayCompaniesEvents: Bool = false) {
        Task { await getData() }
    }
    
    @MainActor func refresh() async {
        do {
            self.errorMessage = nil
            let homeDataSequence = asyncSequence(for: GetHomeDataUseCase().refresh())
            for try await homeData in homeDataSequence {
                data = homeData
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor func getData() async {
        isLoading = true
        do {
            self.errorMessage = nil
            let homeDataSequence = asyncSequence(for: GetHomeDataUseCase().getHome())
            for try await homeData in homeDataSequence {
                isLoading = false
                data = homeData
            }
        } catch {
            isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}
