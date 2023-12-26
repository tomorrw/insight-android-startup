//
//  PostDetailViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/28/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

class PostDetailPageViewModel: DetailPageViewModel {
    let id: String
    
    init(id: String) {
        self.id = id
        super.init()
        Task { await getData() }
    }
    
    @MainActor func getData() async {
        isLoading = true
        do {
            self.errorMessage = nil
            let sessionSequence = asyncSequence(for: GetPostByIdUseCase().getPost(id: id))
            for try await data in sessionSequence {
                self.isLoading = false
                self.title = data.title
                self.date = data.getHumanReadablePublishedAt()
                self.description = data.description_
                self.headerDesign = .detailPage
                self.image = data.image ?? ""
                self.sections = data.detailPage.getDataIfLoaded()?.mapToSectionDisplayInfo() ?? []
                self.socialLinks = nil
                self.action = data.action
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}

