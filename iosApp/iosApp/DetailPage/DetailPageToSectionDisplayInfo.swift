//
//  DetailPageToSectionDisplayInfo.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared

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
                return SectionDisplayInfo.info(InfoContent(title: info.title, description: info.description_, imageUrl: info.image))
            } else if let info = section as? shared.Page.SectionVideoSection {
                return SectionDisplayInfo.video(VideoContent(title: info.title, description: info.description_, videoUrl: info.videoUrl))
            } else if let info = section as? shared.Page.SectionEventList {
                return SectionDisplayInfo.sessions(EventContent(title: info.title, description: "", sessions: info.events))
            } else if let info = section as? shared.Page.SectionSpeakers {
                return SectionDisplayInfo.speakers(SpeakersContent(title: info.title, description: "", speakers: info.speakers))
            } else {
                return nil
            }
        }
    }
}

extension Array where Element == PagePresentationModel {
    func mapToSectionDisplayInfo() -> [SectionDisplayInfo] {
        self.flatMap{ $0.sections }
    }
}
