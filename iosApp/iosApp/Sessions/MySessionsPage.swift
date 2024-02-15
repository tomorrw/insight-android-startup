//
//  MySessionsPage.swift
//  iosApp
//
//  Created by Said on 16/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct MySessionsPage: View {
    @Environment(\.presentationMode) var presentationMode
    @StateObject var vm: MySessionsPageViewModel = MySessionsPageViewModel()
    @State private var isDisplayingError = false
    
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
                        .foregroundColor(info.isEnabled ? Color("Primary") : Color("OnSurface"))
                        .frame(width: 56, height: 56)
                        .background(
                            RoundedRectangle(cornerRadius: 16)
                                .foregroundColor(info.isEnabled ? Color("Default") : Color("Surface"))
                        )
                    }
                    .buttonStyle(.plain)
                    
                    if vm.datesDisplayed.count - 1 != i { Spacer().frame(width: 20) }
                }
            }
            .padding(.top)
            .frame(maxWidth: .infinity)
            .background(Color("Background"))
            
            
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
                VStack{
                    FilterComponent
                    EmptyStateNavLink (
                        title: "No Bookmarked Lectures",
                        text: "Start bookmarking lectures that interest you to keep track of your preferred events. Simply browse the daily lectures schedule and click on the bookmark icon next to the lecture you wish to save. Happy exploring!",
                        destinationView: SessionsPage(startingDisplayDate: vm.currentDate)
                            .navigationTitle("Lectures")
                            .navigationBarTitleDisplayMode(.inline)
                            .onDisappear {
                                Task {
                                    await vm.refresh(keepCurrentDate: true)
                                }
                            },
                        navLinkText: "Go to Lectures"
                    )
                }
            } else {
                ScrollView {
                    FilterComponent
                    SessionsVerticalView(sessions: vm.sessionsDisplayed)
                        .padding(.vertical, 24)
                }
                .id(vm.sessionsDisplayed.hashValue)
                .cornerRadius(16, corners: .topRight)
                .cornerRadius(16, corners: .topLeft)
                .frame(maxHeight: .infinity)
                .refreshable { Task{ await vm.refresh() }}
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
    
    var FilterComponent: some View{
        HStack{
            DropDown(choices: vm.displayedLocation, choiceMade: vm.locationChoice){text in
                vm.filterLocation(location: text)
            }
        }
        .frame(maxHeight: 70)
    }
}
