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

class OfferSearchItem: SearchItem{
    var postId: String?
    
    init(
        id: String,
        title: String,
        description: String,
        image: String?,
        category: SearchCategory? = nil,
        postId: String?
    ) {
        super.init(id: id, title: title, description: description, image: image)
        self.postId = postId
    }
}

class OffersPageViewModel: SearchViewModel{

    
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
                self.changeOriginalList(offers.map{ $0.toOfferSearchItem() })
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
                self.changeOriginalList(offers.map{ $0.toOfferSearchItem() })
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
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
                self.changeOriginalList(offers.map{ $0.toOfferSearchItem() })
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
                self.changeOriginalList(offers.map{ $0.toOfferSearchItem() })
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}
