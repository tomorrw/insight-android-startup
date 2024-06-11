//
//  ToSessionPresentationModel.swift
//  iosApp
//
//  Created by marcjalkh on 29/02/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import UiComponents
import shared
import SwiftUI

extension Array where Element == shared.Session {
    func toSessionPresentationModel() -> [SessionCardModelImplementation] {
        return self.compactMap{session in
            SessionCardModelImplementation(
                timeInterval: session.getTimeInterval(),
                id: session.id,
                title: session.title,
                location: session.location,
                date: session.getDateString(),
                speaker: session.speakers.toEntitiesPresentationModel(),
                tags: [session.getTag()?.toPresentationModel()].compactMap{ $0 },
                minutesAttended: session.minutesAttended?.stringValue
            )
        }
    }
}

extension shared.Session.Tag{
    func toPresentationModel() -> UiComponents.Tag {
        return Tag(
            text: self.text,
            color: Color(hex: "\(self.color.replacingOccurrences(of: "#", with: ""))"),
            background: Color(hex: "\(self.background.replacingOccurrences(of: "#", with: ""))")
        )
    }
}
