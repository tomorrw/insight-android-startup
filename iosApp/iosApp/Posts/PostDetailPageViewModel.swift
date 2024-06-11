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
import DetailPage

class PostDetailPageViewModel: DetailPageViewModel {
    let id: String
    @Published var date: String = ""
    @Published var action: [Action] = []

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
                self.title = data.title
                self.date = data.getHumanReadablePublishedAt()
                self.description = data.description_
                self.headerDesign = .detailPage
                self.image = data.image ?? ""
                self.pages = [data.detailPage.getDataIfLoaded()].compactMap{ $0 }.mapToPagePresentationModel()
                self.action = data.action
                self.isLoading = false
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}

