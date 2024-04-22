//
//  MyProgressPage.swift
//  iosApp
//
//  Created by Yammine on 22/04/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver

struct MyProgressPage: View {
    @InjectedObject var vm: MyProgressPageViewModel
    @Environment(\.sessionCardColors) var colors: SessionCardColors
    @State private var isDisplayingError = false
    
    var body: some View {
        VStack {
            if let sessions = vm.data?.attendedSessions, !sessions.isEmpty , vm.isLoading == false {
                VStack {
                    HStack {
                        HStack(spacing: 12) {
                            Text((vm.data?.league.lecturesAttendedCount ?? 0).description)
                                .font(.system(size: 20, weight: .medium))
                                .multilineTextAlignment(.leading)
                            Text("Lectures Attended")
                                .foregroundColor(colors.secondaryText)
                                .font(.system(size: 14))
                                .multilineTextAlignment(.leading)
                                .frame(width: 60)
                        }
                        
                        Spacer()
                        Rectangle()
                            .frame(width: 1, height: 20) // Adjust the width and height as needed
                            .foregroundColor(colors.secondaryText)
                        Spacer()
                        
                        HStack(spacing: 12) {
                            Text((vm.data?.totalAttendedDuration.toDurationFormatString() ?? ""))
                                .font(.system(size: 20, weight: .medium))
                                .multilineTextAlignment(.leading)
                            Text("Time Spent in Session")
                                .foregroundColor(colors.secondaryText)
                                .font(.system(size: 14))
                                .multilineTextAlignment(.leading)
                                .frame(width: 90)
                        }
                    }
                    
                    ProgressView(value: vm.data?.league.percentage)
                        .tint(Color(hex: "\(vm.data?.league.color ?? "Background")"))
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                }
                .padding(.all)
                .background(colors.background)
                .clipShape(RoundedRectangle(cornerRadius: 16))
                .padding(.horizontal)
                .padding(.bottom, 8)
            }
            
            ScrollView {
                if let sessions = vm.data?.attendedSessions, !sessions.isEmpty {
                    SessionsVerticalView(sessions: sessions, isMinutesDisplayedOnCards: true)
                        .padding(.vertical, 10)
                } else if (vm.isLoading) {
                    VStack(spacing: 16) {
                        ForEach(0..<2, id:\.self) { _ in
                            RoundedRectangle(cornerRadius: 16)
                                .fill(.gray.opacity(0.3))
                                .frame(maxWidth: .infinity)
                                .frame(height: 160)
                        }
                        
                        Spacer()
                    }
                    .frame(maxHeight: .infinity)
                    .padding(.vertical, 24)
                } else {
                    EmptyStateView (
                        title: "No Progress Yet.",
                        text: "After attending lectures, you can assess your progress here.",
                        buttonText: "Reload",
                        buttonAction: {
                            Task { await vm.refresh() }
                        }
                    )
                }
            }
            .refreshable { Task{ await vm.refresh() }}
            .id(vm.data?.attendedSessions.hashValue)
            .frame(maxHeight: .infinity)
            .cornerRadius(16, corners: .topRight)
            .cornerRadius(16, corners: .topLeft)
            .padding(.horizontal)
        }
        .alert("Load Failed.", isPresented: $isDisplayingError, actions: { }, message: {
            Text(vm.errorMessage ?? "Something Went Wrong!")
        })
        .onReceive(vm.$errorMessage, perform: { error in
            guard error != nil && error != "" else {
                isDisplayingError = false
                return
            }
            isDisplayingError = true
        })
        .background(Color("Background"))
        .navigationTitle("My Progress")
        .navigationBarTitleDisplayMode(.inline)
    }
}
