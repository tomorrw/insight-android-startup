//
//  SessionsVerticalView.swift
//  iosApp
//
//  Created by Yammine on 4/26/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import KMPNativeCoroutinesAsync

struct SessionsVerticalView: View {
    let sessions: [Session]
    let isMinutesDisplayedOnCards: Bool
    
    init(sessions: [Session], isMinutesDisplayedOnCards: Bool = false) {
        self.sessions = sessions
        self.isMinutesDisplayedOnCards = isMinutesDisplayedOnCards
    }
    
    var body: some View {
        LazyVStack(alignment: .leading, spacing: 16) {
            ForEach(sessions.indices, id: \.self) { i in
                let session = sessions[i]
                
                NavigateTo {
                    SessionDetailPage(id: session.id)
                } label: {
                    SessionCard(session: session, isMinutesAttendedDisplayed: isMinutesDisplayedOnCards)
                }
                .buttonStyle(FlatLinkStyle())
            }
        }
    }
    
    @MainActor func toggleShouldSendReminder(id: String) async {
        let _ = await asyncResult(for: ToggleShouldNotifyEventUseCase().toggleShouldNotify(id: id))
    }
}

struct SessionsVerticalView_Previews: PreviewProvider {
    static var previews: some View {
        SessionsVerticalView(sessions: [], isMinutesDisplayedOnCards: false)
    }
}
