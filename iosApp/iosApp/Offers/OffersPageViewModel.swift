//
//  OffersPageViewModel.swift
//  iosApp
//
//  Created by marcjalkh on 20/09/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

class OffersPageViewModel: ObservableObject {
    @Published var data: [Offer] = []
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil
    
    init(){
        Task{ await getOffers() }
    }
    
    @MainActor func refresh() async {
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetOffersUseCase().refresh())
            for try await offers in sequence {
                data = offers
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor func getOffers() async{
        self.isLoading = true
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetOffersUseCase().getOffers())
            for try await offers in sequence {
                self.isLoading = false
                data = offers
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}

class ClaimedOffersPageViewModel: OffersPageViewModel {
    @MainActor override func refresh() async {
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetOffersUseCase().refreshClaimedOffers())
            for try await offers in sequence {
                data = offers
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor override func getOffers() async{
        self.isLoading = true
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetOffersUseCase().getClaimedOffers())
            for try await offers in sequence {
                self.isLoading = false
                data = offers
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}
