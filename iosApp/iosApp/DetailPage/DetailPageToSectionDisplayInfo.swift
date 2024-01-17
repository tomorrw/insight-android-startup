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
    func mapToSectionDisplayInfo() -> [SectionDisplayInfo] {
        self.flatMap { $0.mapToSectionDisplayInfo() }
    }
}

extension Page {
    func mapToSectionDisplayInfo() -> [SectionDisplayInfo] {
        return self.sections.compactMap { section in
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
