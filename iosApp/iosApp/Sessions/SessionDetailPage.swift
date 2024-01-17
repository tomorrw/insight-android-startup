//
//  SessionPaeg.swift
//  iosApp
//
//  Created by Yammine on 4/26/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import Firebase
import KMPNativeCoroutinesAsync

struct SessionDetailPage: View {
    @StateObject private var vm: SessionDetailPageViewModel
    
    @State var isConfirmBookmarkDisplayed: Bool = false
    @State var shouldNotify: Bool = true
    @State var overlappingLectureName: String? = nil
    
    var sessionId: String
    
    init(id: String) {
        _vm = StateObject(wrappedValue: SessionDetailPageViewModel(id: id))
        sessionId = id
    }
    
    var body: some View {
        DetailPage(isSessionDetails: true, vm: vm)
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
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
                        .foregroundColor(.accentColor)
                }
                .frame(width: 50, height: 38)
                .padding(.top, 6)
            }
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
            .background(Color("Background"))
    }
    
    var confirmBookmarkDescription: String {
        if let lectureName = overlappingLectureName {
            return "The lecture you are trying to bookmark overlaps with your bookmarked lecture: `\(lectureName)`"
        } else {
            return "The lecture you are trying to bookmark overlaps with one of your bookmarked lectures"
        }
    }
    
    @MainActor func toggleShouldSendReminder() async {
        let _ = await asyncResult(for: ToggleShouldNotifyEventUseCase().toggleShouldNotify(id: sessionId))
        withAnimation {
            shouldNotify = ShouldNotifyEventUseCase().shouldNotify(id: sessionId)
        }
        do {
            let messaging = Messaging.messaging()
            let topic = "session_\(sessionId)"
            try await shouldNotify ? messaging.subscribe(toTopic: topic) : messaging.unsubscribe(fromTopic: topic)
        } catch {}
    }
    
    func checkIfShouldNotify() {
        shouldNotify = ShouldNotifyEventUseCase().shouldNotify(id: sessionId)
    }
    
    func doSessionOverlap() -> Bool {
        let overlappingSession: Session? = OverlappingSessionUseCase().getOverlapping(id: sessionId) as Session?
        
        overlappingLectureName = overlappingSession?.title
        
        return overlappingSession != nil
    }
}
