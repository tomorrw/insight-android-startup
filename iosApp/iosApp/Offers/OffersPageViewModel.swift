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

class OffersPageViewModel: SearchViewModel{
    var offerList: [Offer] = []{
        didSet{
            Task { await self.changeOriginalList(offerList.map{ $0.toSearchItem() })
            }
        }
    }
    
    init() {
        super.init(list: [], searchText: "")
        Task { await getOffers() }
    }
    
    @MainActor func getOffers() async {
        self.isLoading = true
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetOffersUseCase().getOffers())
            for try await offers in sequence {
                self.isLoading = false
                offerList = offers
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor override func refreshData() async {
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetOffersUseCase().refresh())
            for try await offers in sequence {
                offerList = offers
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    func getPostId(id: String)-> String? {
        return self.offerList.first(where: {$0.id == id})?.postId ?? nil
    }
}

class ClaimedOffersPageViewModel: OffersPageViewModel{
    @MainActor override func getOffers() async {
        self.isLoading = true
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetOffersUseCase().getClaimedOffers())
            for try await offers in sequence {
                self.isLoading = false
                offerList = offers
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor override func refreshData() async {
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetOffersUseCase().refreshClaimedOffers())
            for try await offers in sequence {
                offerList = offers
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}
