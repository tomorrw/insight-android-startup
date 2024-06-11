//
//  CompanyPageViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync
import DetailPage

class CompanyPageViewModel: DetailPageViewModel {
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
            let companySequence = asyncSequence(for: GetCompanyByIdUseCase().getCompany(id: id))
            for try await data in companySequence {
                let detailPages = data.detailPages.getDataIfLoaded()

                self.isLoading = false
                self.title = data.title
                self.description = data.boothDescription != nil ? "\(data.objectsClause)" : "\(data.objectsClause) \n\(String(describing: data.boothDescription))"
                self.headerDesign = .contact
                self.image = data.image ?? ""
                self.socialLinks = data.socialLinks.mapToSocialLinkUI()
                self.pages = detailPages?.compactMap { $0 as? Page }.mapToPagePresentationModel() ?? []
                
            }
        } catch {
            self.isLoading = false
            self.errorMessage = (error as? KotlinThrowable)?.toUserFriendlyError() ?? "Something Went Wrong!"
        }
    }
}
