//
//  SectionDisplayViewImplementation.swift
//  iosApp
//
//  Created by marcjalkh on 07/06/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI
import UiComponents
import DetailPage

struct SectionDisplayViewImplementation: View {
    var section: SectionDisplayInfo
    var sessionCardColor: EventCardColors = DefaultColors.sessionCardColor
    var body: some View {
        SectionDisplayView(
            section: section) { sessions in
                SessionsVerticalView(sessions: sessions, cardColors: sessionCardColor)
            } entitiesView: { speakers in
                SpeakersHorizontalView(speakers)
            }
    }
}
