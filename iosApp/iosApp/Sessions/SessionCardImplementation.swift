//
//  SessionCardImplementation.swift
//  iosApp
//
//  Created by marcjalkh on 07/03/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI
import UiComponents
import ImageCaching
import shared
import Firebase
import KMPNativeCoroutinesAsync

struct SessionCardImplementation: View {
    @State var shouldNotify: Bool = false
    @State var overlappingLectureName: String? = nil
    @State var isConfirmBookmarkDisplayed: Bool = false
    var isMinutesAttendedDisplayed: Bool = false
    var tag: Tag? = nil
    var session: EventModel
    let cardColors: EventCardColors
    var confirmBookmarkDescription: String {
        if let lectureName = overlappingLectureName {
            return "The lecture you are trying to bookmark overlaps with your bookmarked lecture: `\(lectureName)`"
        } else {
            return "The lecture you are trying to bookmark overlaps with one of your bookmarked lectures"
        }
    }
    
    var body: some View {
        EventCard(
            event: session,
            colors: cardColors,
            topRightItem: {
                if isMinutesAttendedDisplayed, let session = session as? SessionCardModelImplementation,let minutes = session.minutesAttended  {
                    Text("\(minutes)m Attended")
                        .foregroundColor(tag?.color)
                        .font(.system(size: 14))
                        .padding(.horizontal)
                        .padding(.top)
                        .padding(.bottom, 8)
                }
                else {
                    Button {
                        Task {
                            if !shouldNotify,
                               let ovelappingSession = OverlappingSessionUseCase().getOverlapping(id: session.id)?.title {
                                overlappingLectureName = ovelappingSession
                                isConfirmBookmarkDisplayed.toggle()
                            }
                            else {
                                await self.toggleShouldSendReminder(id: session.id)
                            }
                        }
                    } label: {
                        Image(systemName: shouldNotify ? "bookmark.fill": "bookmark")
                            .resizable()
                            .frame(width: 14, height: 20)
                            .foregroundColor(Color("Primary"))
                    }
                    .frame(width: 50, height: 38)
                    .padding(.top, 6)
                }
            },
            entityView: { speaker in
                NavigateTo {
                    SpeakerDetailPage(id: speaker.id)
                } label: {
                    HStack {
                        UrlImageView(urlString: speaker.secondaryImage)
                            .scaledToFill()
                            .clipShape(Circle())
                            .frame(width: 18, height: 18)
                        Text(speaker.name)
                            .font(.system(size: 16))
                    }
                }
            }
        )
        .onAppear{ checkIfNotify(id: session.id) }
        .alert(isPresented: $isConfirmBookmarkDisplayed) {
            Alert(
                title: Text("OverLapping Lectures"),
                message: Text(confirmBookmarkDescription),
                primaryButton: .destructive(Text("Add anyway").foregroundColor(.accentColor)) {
                    Task {
                        await toggleShouldSendReminder(id: session.id)
                    }
                },
                secondaryButton: .cancel()
            )
        }
    }
    
    @MainActor func toggleShouldSendReminder(id: String) async {
        do {
            let _ = await asyncResult(for: ToggleShouldNotifyEventUseCase().toggleShouldNotify(id: id))
            checkIfNotify(id: id)
            
            let messaging = Messaging.messaging()
            let topic = "session_\(id)"
            try await ShouldNotifyEventUseCase().shouldNotify(id: id) ? messaging.subscribe(toTopic: topic) : messaging.unsubscribe(fromTopic: topic)
        } catch {}
    }
    
    @MainActor func checkIfNotify(id: String){
        self.shouldNotify = ShouldNotifyEventUseCase().shouldNotify(id: id)
    }
}


class SessionCardModelImplementation: EventModel{
    var minutesAttended: String?
    
    init(
        timeInterval: String,
        id: String,
        title: String,
        location: String,
        date: String,
        speaker: [EntityModel],
        tags: [Tag] = [],
        minutesAttended: String? = nil
    ) {
        super.init(
            timeInterval: timeInterval,
            id: id,
            title: title,
            location: location,
            date: date,
            entities: speaker,
            tags: tags
        )
        self.minutesAttended = minutesAttended
    }
}
