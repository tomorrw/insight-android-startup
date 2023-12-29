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

struct SessionCard: View {
    let session: Session
    var date: String = ""
    
    @State var isConfirmBookmarkDisplayed: Bool = false
    @State var shouldNotify: Bool = true
    @State var overlappingLectureName: String? = nil
    
    var confirmBookmarkDescription: String {
        if let lectureName = overlappingLectureName {
            return "The lecture you are trying to bookmark overlaps with your bookmarked lecture: `\(lectureName)`"
        } else {
            return "The lecture you are trying to bookmark overlaps with one of your bookmarked lectures"
        }
    }
    
    init(session: Session) {
        self.session = session
        
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
                    .foregroundColor(Color("HighlightPrimary"))
                    .padding(.top)
                    .padding(.leading)
                
                Spacer()
                
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
                        .foregroundColor(Color("Primary"))
                }
                .frame(width: 50, height: 38)
                .padding(.top, 6)
            }
            .foregroundColor(.gray)
            
            VStack(alignment: .leading) {
                Text(session.title)
                    .font(.system(size: 20, weight: .medium))
                    .multilineTextAlignment(.leading)
                    .padding(.bottom, 10)
//                Text(session.topic)
//                    .font(.system(size: 18, weight: .light))
//                    .multilineTextAlignment(.leading)
//                    .padding(.bottom, 10)
//                    .foregroundColor(Color("HighlightNative"))
                    
                
                Group {
                    HStack {
                        Image(systemName: "location")
                            .foregroundColor(Color("Primary"))
                        Text(session.location)
                            .multilineTextAlignment(.leading)
                    }
                    
                    HStack {
                        Image(systemName: "calendar")
                            .foregroundColor(Color("Primary"))
                        Text(date)
                    }
                }
                .padding(.bottom, 4)
                .foregroundColor(Color("Secondary"))
                .font(.system(size: 14))
                
                HStack {
                    if let speaker = session.speakers.first {
                        NavigateTo {
                            SpeakerDetailPage(id: speaker.id)
                        } label: {
                            HStack {
                                UrlImageView(urlString: speaker.nationality?.url)
                                    .scaledToFill()
                                    .clipShape(Circle())
                                    .frame(width: 18, height: 18)
                                Text(speaker.getFullName())
                                    .font(.system(size: 16))
                            }
                        }
                                
                        Spacer()
                        
                        if session.hasAttended {
                            Text("ATTENDED")
                                .font(.system(size: 14))
                                .padding(5)
                                .background(
                                    RoundedRectangle(cornerRadius: 8)
                                        .foregroundColor(Color("ColoredDefault"))
                                )
                        }
                        
                        if session.isSessionHappeningNow() {
                            Text("NOW")
                                .font(.system(size: 14))
                                .foregroundColor(.white)
                                .padding(5)
                                .background(
                                    RoundedRectangle(cornerRadius: 8)
                                        .foregroundColor(.accentColor)
                                )
                        }
                    }
                }
                .padding(.bottom, 4)
            }
            .padding(.horizontal)
            .padding(.bottom)
        }
        .frame(maxWidth: .infinity)
        .background(Color("Default"))
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
