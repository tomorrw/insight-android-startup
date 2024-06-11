//
//  DetailPageToSectionDisplayInfo.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared
import DetailPage
import UiComponents

extension Array where Element == Page {
    func mapToPagePresentationModel() -> [PagePresentationModel] {
        return self.compactMap{
            PagePresentationModel(title: $0.title, sections: $0.sections.mapToSectionDisplayInfo())
        }
    }
}


extension Array where Element == Page.Section {
    func mapToSectionDisplayInfo() -> [SectionDisplayInfo] {
        return self.compactMap { section in
            if let info = section as? shared.Page.SectionInfoSection {
                return SectionDisplayInfo.info(SectionModel(title: info.title, description: info.description_, data: InfoModel(imageUrl: info.image)))
            } else if let info = section as? shared.Page.SectionVideoSection {
                return SectionDisplayInfo.video(SectionModel(title: info.title, description: info.description_, data: VideoModel(videoUrl: info.videoUrl)))
            } else if let info = section as? shared.Page.SectionEventList {
                return SectionDisplayInfo.events(SectionModel(title: info.title, description: "", data: info.events.toSessionPresentationModel()))
            } else if let info = section as? shared.Page.SectionSpeakers {
                return SectionDisplayInfo.people(SectionModel(title: info.title, description: "", data: info.speakers.toEntitiesPresentationModel()))
            } else {
                return nil
            }
        }
    }
}

extension Array where Element == shared.Speaker {
    func toEntitiesPresentationModel() -> [EntityModel] {
        return self.compactMap{speaker in
            EntityModel(
                id: speaker.id,
                name: speaker.fullName.getFullName(),
                image: speaker.image,
                secondaryImage: speaker.nationality?.url
            )
        }
    }
}

extension Array where Element == PagePresentationModel {
    func mapToSectionDisplayInfo() -> [SectionDisplayInfo] {
        self.flatMap{ $0.sections }
    }
}
