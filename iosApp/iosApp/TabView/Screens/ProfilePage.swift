//
//  ProfilePage.swift
//  iosApp
//
//  Created by marcjalkh on 02/10/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver
import UiComponents

struct ProfilePage: View {
    @InjectedObject var authViewModel: AuthenticationViewModel
    @State private var isDisplayingError = false
    
    var body: some View {
        NavigationPages() {
            VStack {
                HStack {
                    VStack(alignment: .leading) {
                        Text("Profile")
                            .font(.system(size: 24, weight: .bold))
                        Text("Track your progress and manage your profile")
                            .foregroundColor(Color("Secondary"))
                    }
                    Spacer()
                }
                .padding(.horizontal)
                .padding(.top, 8)
                
                ScrollView{
                    VStack(spacing: 16) {
                        NavigateTo(destination: { 
                            ProfileSettingsPage()
                                .navigationTitle("Profile Settings")
                                .navigationBarTitleDisplayMode(.inline)
                        }) {
                            HighlightedCard(
                                image: "",
                                title: authViewModel.user?.fullName.getFormattedName() ?? "User",
                                description: authViewModel.user?.phoneNumber.number ?? "",
                                cardColor: DefaultColors.defaultCardColor
                            )
                        }
                        .padding(.top, 10)
                        
                        CircularProgressView(
                            progress: Double((authViewModel.user?.league.percentage) == 0 ? 0.01 : authViewModel.user?.league.percentage ?? 0),
                            style: CircularProgressViewStyle(
                                progressBarColor: Color(hex: "\(authViewModel.user?.league.color ?? "Default")"),
                                background: Color("Default"),
                                foreground: Color("Primary")
                                ),
                            title: "\(authViewModel.user?.league.lecturesAttendedCount.description ?? "")/\(authViewModel.user?.league.totalNumberOfLectures.description ?? "")",
                            subTitle: "LECTURES ATTENDED"
                        )

                            .padding(.top,20)
                        
                        if let leagueName = authViewModel.user?.league.name {
                            Text(leagueName)
                                .foregroundColor(Color(hex: "\(authViewModel.user?.league.color ?? "Background")"))
                                .font(.system(size: 22))
                                .fontWeight(.bold)
                        }
                        
                        Spacer()
                        
                        
                        ActionButtons(actions: authViewModel.user?.actions ?? [] )
                            .buttonStyle(.plain)
                    }
                    .padding(.horizontal)
                    .padding(.bottom)
                }
                .refreshable{ Task{ authViewModel.getUser() } }
            }
            .navigationTitle("Profile")
            .frame(maxWidth: .infinity)
            .navigationBarHidden(true)
            .background(Color("Background"))
        }
    }
}

struct ProfilePage_Previews: PreviewProvider {
    static var previews: some View {
        ProfilePage()
    }
}
