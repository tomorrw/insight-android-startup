//
//  SessionsVerticalView.swift
//  iosApp
//
//  Created by Yammine on 4/26/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import UiComponents
import ImageCaching

struct SessionsVerticalView: View {
    let sessions: [EventModel]
    let cardColors: EventCardColors
    var isMinutesDisplayedOnCards = false
    
    init(sessions: [EventModel], cardColors: EventCardColors = DefaultColors.sessionCardColor, isMinutesDisplayedOnCards: Bool = false) {
        self.sessions = sessions
        self.isMinutesDisplayedOnCards = isMinutesDisplayedOnCards
        self.cardColors = cardColors
    }
    
    var body: some View {
        //A Lazy Vstack is not loading the bookmark states correctly
        ScrollView() {
            ForEach(sessions, id: \.self) { session in
                NavigateTo {
                    SessionDetailPage(id: session.id)
                } label: {
                    SessionCardImplementation(isMinutesAttendedDisplayed: isMinutesDisplayedOnCards, session: session, cardColors: cardColors)
                }
                .buttonStyle(FlatLinkStyle())
                .padding(.vertical, 4)
            }
        }
    }

}

struct SessionsVerticalView_Previews: PreviewProvider {
    static var previews: some View {
        SessionsVerticalView(sessions: [], isMinutesDisplayedOnCards: false)
    }
}
