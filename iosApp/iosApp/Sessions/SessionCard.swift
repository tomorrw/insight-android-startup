//
//  SessionCard.swift
//  iosApp
//
//  Created by Said on 16/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import KMPNativeCoroutinesAsync
import Firebase


struct SessionCardColors{
    var primaryText: Color = Color("Primary")
    var secondaryText: Color = Color("Secondary")
    var highlightedText: Color = Color("HighlightPrimary")
    var icons: Color = Color("Primary")
    var background: Color = Color("Default")
    var firstTag: TextTagColor = TextTagColor()
    var secondaryTag: TextTagColor = TextTagColor(textColor: Color("Default"),background: .accentColor)
}

struct SessionCard: View {
    let session: Session
    let isMinutesAttendedDisplayed: Bool
    var date: String = ""
    let tag: Tag?
    @State var isConfirmBookmarkDisplayed: Bool = false
    @State var shouldNotify: Bool = true
    @State var overlappingLectureName: String? = nil
    @Environment(\.sessionCardColors) var colors: SessionCardColors
    
    var confirmBookmarkDescription: String {
        if let lectureName = overlappingLectureName {
            return "The lecture you are trying to bookmark overlaps with your bookmarked lecture: `\(lectureName)`"
        } else {
            return "The lecture you are trying to bookmark overlaps with one of your bookmarked lectures"
        }
    }
    
    init(session: Session, isMinutesAttendedDisplayed: Bool = false) {
        self.session = session
        self.isMinutesAttendedDisplayed = isMinutesAttendedDisplayed
        if let tag = session.getTag() { self.tag = Tag(tag: tag) }
        else { self.tag = nil }
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        if let date = dateFormatter.date(from: session.getDateString()) {
            dateFormatter.dateFormat = "MMM d, yyyy"
            self.date = dateFormatter.string(from: date)
        }
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack(alignment: .top) {
                Text(session.getTimeInterval())
                    .font(.system(size: 14))
                    .foregroundColor(colors.highlightedText)
                    .padding(.top)
                    .padding(.leading)
                
                Spacer()
                
                if isMinutesAttendedDisplayed , let minutes = session.minutesAttended {
                    Text("\(minutes.description)m Attended")
                        .foregroundColor(tag?.color)
                        .font(.system(size: 14))
                        .padding(.horizontal)
                        .padding(.top)
                } else {
                    Button {
                        Task {
                            if shouldNotify == false && doSessionOverlap() {
                                isConfirmBookmarkDisplayed = true
                            } else {
                                await toggleShouldSendReminder()
                            }
                        }
                    } label: {
                        Image(systemName: shouldNotify ? "bookmark.fill": "bookmark")
                            .resizable()
                            .frame(width: 14, height: 20)
                            .foregroundColor(colors.icons)
                    }
                    .frame(width: 50, height: 38)
                    .padding(.top, 6)
                }
            }
            .foregroundColor(.gray)
            
            VStack(alignment: .leading) {
                Text(session.title)
                    .font(.system(size: 20, weight: .medium))
                    .multilineTextAlignment(.leading)
                    .padding(.bottom, 10)
                
                
                Group {
                    HStack {
                        Image(systemName: "location")
                            .foregroundColor(colors.icons)
                        Text(session.location)
                            .multilineTextAlignment(.leading)
                    }
                    
                    HStack {
                        Image(systemName: "calendar")
                            .foregroundColor(colors.icons)
                        Text(date)
                        
                        Spacer()
                        
                        if session.speakers.isEmpty, let tag = tag {
                            TextTag(
                                text: tag.text,
                                colors: .init(textColor: tag.color, background: tag.background)
                            )
                        }
                    }
                }
                .padding(.bottom, 4)
                .foregroundColor(colors.secondaryText)
                .font(.system(size: 14))
                
                HStack {
                    if !session.speakers.isEmpty {
                        ForEach(session.speakers) { speaker in
                            NavigateTo {
                                SpeakerDetailPage(id: speaker.id)
                            } label: {
                                HStack {
                                    UrlImageView(urlString: speaker.nationality?.url)
                                        .scaledToFill()
                                        .clipShape(Circle())
                                        .frame(width: 18, height: 18)
                                    Text(speaker.fullName.getFormattedName())
                                        .font(.system(size: 16))
                                }
                            }
                            
                            if session.speakers.last == speaker {
                                Spacer()
                                
                                if !session.speakers.isEmpty,  let tag = tag {
                                    TextTag(
                                        text: tag.text,
                                        colors: .init(textColor: tag.color, background: tag.background)
                                    )
                                }
                            }
                        }
                    }
                }
                .padding(.bottom, 4)
            }
            .padding(.horizontal)
            .padding(.bottom)
        }
        .foregroundColor(colors.primaryText)
        .frame(maxWidth: .infinity)
        .background(colors.background)
        .clipShape(RoundedRectangle(cornerRadius: 16))
        .onAppear {
            self.checkIfShouldNotify()
        }
        .alert(isPresented: $isConfirmBookmarkDisplayed) {
            Alert(
                title: Text("OverLapping Lectures"),
                message: Text(confirmBookmarkDescription),
                primaryButton: .destructive(Text("Add anyway").foregroundColor(.accentColor)) {
                    Task {
                        await toggleShouldSendReminder()
                    }
                },
                secondaryButton: .cancel()
            )
        }
    }
    
    @MainActor func toggleShouldSendReminder() async {
        let _ = await asyncResult(for: ToggleShouldNotifyEventUseCase().toggleShouldNotify(id: session.id))
        withAnimation {
            shouldNotify = ShouldNotifyEventUseCase().shouldNotify(id: session.id)
        }
        do {
            let messaging = Messaging.messaging()
            let topic = "session_\(session.id)"
            try await shouldNotify ? messaging.subscribe(toTopic: topic) : messaging.unsubscribe(fromTopic: topic)
        } catch {}
    }
    
    func checkIfShouldNotify() {
        shouldNotify = ShouldNotifyEventUseCase().shouldNotify(id: session.id)
    }
    
    func doSessionOverlap() -> Bool {
        let overlappingSession: Session? = OverlappingSessionUseCase().getOverlapping(id: session.id) as Session?
        
        overlappingLectureName = overlappingSession?.title
        
        return overlappingSession != nil
    }
}
