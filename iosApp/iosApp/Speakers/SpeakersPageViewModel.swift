//
//  SpeakersPageViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import shared
import KMPNativeCoroutinesAsync

class SpeakersViewModel: SearchViewModel {
    init() {
        super.init(list: [], categorize: true, searchText: "")
        Task { await getSpeakers() }
    }
    
    @MainActor override func refreshData() async {
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetSpeakersUseCase().refresh())
            for try await speakers in sequence {
                self.changeOriginalList(mapToSearchList(speakers), categorize: true)
            }
        } catch {
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    @MainActor func getSpeakers() async {
        self.isLoading = true
        do {
            self.errorMessage = nil
            let sequence = asyncSequence(for: GetSpeakersUseCase().getSpeakers())
            for try await speakers in sequence {
                self.isLoading = false
                self.changeOriginalList(mapToSearchList(speakers), categorize: true)
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
    
    func mapToSearchList(_ allData: [Speaker]) -> [SearchItem] {
        allData.map {
            SearchItem(
                id: $0.id,
                title: $0.fullName.getFormattedName(),
                description: $0.title,
                image: $0.image,
                category: SearchCategory($0.nationality?.name ?? "", image: $0.nationality?.url)
            )
        }.sorted { (item1, item2) in
            guard let text1 = item1.category?.text, let text2 = item2.category?.text else {
                return false
            }
            return text1 < text2
        }
        
    }
}
