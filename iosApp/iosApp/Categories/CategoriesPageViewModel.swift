//
//  CategoriesPageViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import shared
import KMPNativeCoroutinesAsync
import SearchableList

class CategoriesViewModel: SearchViewModelImplementation {
    init() {
        super.init()
        Task { await getCategories() }
    }
    
    @MainActor override func refreshData() async {
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetCategoriesUseCase().refresh())
            for try await allCategories in sequence {
                self.changeOriginalList(mapToSearchList(allCategories))
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor func getCategories() async {
        self.isLoading = true
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetCategoriesUseCase().getCategories())
            for try await allCategories in sequence {
                self.isLoading = false
                self.changeOriginalList(mapToSearchList(allCategories))
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    func mapToSearchList(_ allData: [Company.Category]) -> [SearchItem] {
        allData.map {
            SearchItem(
                id: $0.id,
                title: $0.name,
                description: $0.description_,
                image: nil
            )
        }
    }
}
