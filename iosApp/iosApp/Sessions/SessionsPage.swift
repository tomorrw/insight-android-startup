//
//  SessionsPage.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared

struct SessionsPage: View {
    @StateObject var vm: SessionsPageViewModel = SessionsPageViewModel()
    @State private var isDisplayingError = false
    var startingDisplayDate: Kotlinx_datetimeLocalDate? = nil
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                ForEach(vm.datesDisplayed.indices, id: \.self) { i in
                    let info = vm.datesDisplayed[i]
                    Button {
                        withAnimation { vm.changeDate(info.date) }
                    } label: {
                        VStack {
                            Text("\(info.date.dayOfMonth)")
                                .font(.system(size: 16, weight: .medium))
                            Text(info.date.month.description().capitalized.prefix(3))
                                .font(.system(size: 14))
                        }
                        .foregroundColor(info.isEnabled ? Color("Primary") : Color("Secondary"))
                        .frame(width: 56, height: 56)
                        .background(
                            RoundedRectangle(cornerRadius: 16)
                                .foregroundColor(info.isEnabled ? Color("Default") : Color("Dark"))
                        )
                    }
                    
                    if vm.datesDisplayed.count - 1 != i { Spacer().frame(width: 20) }
                }
                .onAppear {
                    guard let startingDisplayDate = startingDisplayDate else {
                        return
                    }
                    vm.changeDate(startingDisplayDate)
                }
            }
            .frame(maxWidth: .infinity)
            .background(Color("Background"))
            .padding(.top)
            
            
            if (vm.isLoading) {
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
            } else if vm.sessionsDisplayed.isEmpty {
                EmptyStateView (
                    title: "Nothing here.",
                    text: "Stay tuned for more!",
                    buttonText: "Reload",
                    buttonAction: {
                        Task { await vm.refresh() }
                    }
                )
            } else {
                ScrollView {
                    HStack{
                        DropDown(choices: vm.displayedLocation, choiceMade: vm.locationChoice){text in
                            vm.filterLocation(location: text)
                        }
                    }

                    SessionsVerticalView(sessions: vm.sessionsDisplayed)
                        .padding(.vertical, 10)
                }
                .refreshable { Task{ await vm.refresh() }}
                .id(vm.sessionsDisplayed.hashValue)
                .cornerRadius(16, corners: .topRight)
                .cornerRadius(16, corners: .topLeft)
                .frame(maxHeight: .infinity)
            }
        }
        .padding(.horizontal)
        .frame(maxWidth: .infinity)
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
    }
}




