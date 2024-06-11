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
import DetailPage
import UiComponents

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
                HStack {
                    if let tag = vm.tag {
                        TextTag(
                            text: tag.text,
                            colors: .init(foreground: tag.color, background: tag.background)
                        )
                        
                        Spacer()
                        
                        if let minutes = vm.minutesAttended {
                            Text("\(minutes.description)m Attended")
                                .foregroundColor(tag.color)
                                .font(.system(size: 14))
                        }
                    }
                }
                
                sessionHeader
            },
            customBody: {
                if vm.pages.first != nil {
                    VerticalSectionsView(
                        sections: $vm.pages.first!.sections,
                        SectionDisplayView: {
                            SectionDisplayViewImplementation(section: $0, sessionCardColor: DefaultColors.sessionCardColorVariation)
                        },
                        customFooter: { ActionButtons(actions: vm.action) }
                    )
                }
            }
        )
        .refreshable{ await self.vm.getData() }
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
            if let attendees = vm.attendees{
                Label(title: {
                    Text(attendees)
                        .foregroundColor(.gray)
                }) {
                    Image(systemName: "person.fill")
                        .foregroundColor(.accentColor)
                }
            }
            
            
            
            if vm.canAskQuestions {
                NavigateTo(destination: {
                    AskAQuestionPage(
                        subjectId: vm.subjectId,
                        title: vm.title,
                        speakersFullName: vm.speakers.compactMap{ $0.name }
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
                        .padding(.top, 5)
                    
                })
            }
        }
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
        let overlappingSession: shared.Session? = OverlappingSessionUseCase().getOverlapping(id: sessionId) as shared.Session?
        
        overlappingLectureName = overlappingSession?.title
        
        return overlappingSession != nil
    }
}
