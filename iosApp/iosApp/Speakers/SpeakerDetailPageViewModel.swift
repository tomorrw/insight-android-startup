//
//  SpeakerDetailPageViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import KMPNativeCoroutinesAsync


class SpeakerDetailPageViewModel: DetailPageViewModel {
    let id: String
    @Published var socialLinks: [SocialLink] = []

    init(id: String) {
        self.id = id
        super.init()
        Task { await getData() }
    }
    
    @MainActor func getData() async {
        isLoading = true
        do {
            self.errorMessage = nil
            let speakerSequence = asyncSequence(for: GetSpeakerByIdUseCase().getSpeaker(id: id))
            for try await data in speakerSequence {
                let detailPages = data.detailPages.getDataIfLoaded()
                
                self.isLoading = false
                self.title = data.getFullName()
                self.description = data.title
                if let country = data.nationality?.name {
                    self.description += " | \(country)"
                }
                self.headerDesign = .contact
                self.image = data.image ?? ""
                self.socialLinks = data.socialLinks.mapToSocialLinkUI()
                if let icon = data.nationality?.url {
                    self.imagePinIcon = icon
                }
                self.sections = detailPages?.compactMap { $0 as? Page }.mapToSectionDisplayInfo() ?? []
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}
