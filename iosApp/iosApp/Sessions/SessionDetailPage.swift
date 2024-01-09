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
        DetailPage(
            vm: vm,
            customHeader: { 
                if vm.hasAttended {
                    Text("ATTENDED")
                        .foregroundColor(Color("Secondary"))
                        .font(.system(size: 14))
                        .padding(5)
                        .background(
                            RoundedRectangle(cornerRadius: 8)
                                .foregroundColor(.accentColor)
                        )
                }
                sessionHeader
            },
            customBody: { VerticalDisplayView(sections: $vm.sections, actions: $vm.action) }
        )
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
    
    var sessionHeader: some View {
        Group {
            Label(title: {
                Text(vm.location)
                    .foregroundColor(.gray)
            }) {
                Image(systemName: "location")
                    .foregroundColor(.accentColor)
            }
            
            Label(title: {
                Text(vm.date)
                    .foregroundColor(.gray)
            }) {
                Image(systemName: "calendar")
                    .foregroundColor(.accentColor)
            }
            
            Label(title: {
                Text(vm.timeInterval)
                    .foregroundColor(.gray)
            }) {
                Image(systemName: "clock")
                    .foregroundColor(.accentColor)
            }
            
            Label(title: {
                Text(vm.attendees)
                    .foregroundColor(.gray)
            }) {
                Image(systemName: "person.fill")
                    .foregroundColor(.accentColor)
            }
            .padding(.bottom, 5)
            
            
            
            if vm.canAskQuestions {
                NavigateTo(destination: {
                    AskAQuestionPage(
                        subjectId: vm.subjectId,
                        title: vm.title,
                        speakers: (vm.sections.first(where: { section in
                            switch section {
                            case .speakers(_): return true
                            default: return false
                            }
                        })?.getInfo() as? SpeakersContent)?.speakers
                    )
                    .navigationTitle("Post Your Question")
                }, label: {
                    Text("Ask a Question")
                        .foregroundColor(Color("Default"))
                        .font(.system(size: 16))
                        .frame(maxWidth: .infinity)
                        .padding(16)
                        .background(Color("Primary"))
                        .cornerRadius(16)
                })
            }}
        .font(.system(size: 14))
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
