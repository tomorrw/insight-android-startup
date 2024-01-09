//
//  ProfilePage.swift
//  iosApp
//
//  Created by marcjalkh on 02/10/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver


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
                        NavigateTo(destination: { ProfileSettingsPage() }) {
                            HighlightedCard(
                                image: "",
                                title: "Dr. White",
                                description: authViewModel.user?.phoneNumber.number ?? ""
                            )
                        }
                        .padding(.top, 10)
                        
                        CircularProgressView(progress: Double((authViewModel.user?.league.percentage) == 0 ? 0.01 : authViewModel.user?.league.percentage ?? 0), color: Color(hex: "\(authViewModel.user?.league.color ?? "Default")"))
                            .padding(.all, 25)
                            .overlay(alignment: .center) {
                                VStack {
                                    Text("\(authViewModel.user?.league.lecturesAttendedCount.description ?? "")/\(authViewModel.user?.league.totalNumberOfLectures.description ?? "")")
                                        .font(.system(size: 70))
                                        .multilineTextAlignment(.center)
                                    
                                    Text("LECTURES ATTENDED")
                                        .font(.title3)
                                        .multilineTextAlignment(.center)
                                }
                            }
                            .background { Circle().fill(Color("Default")) }
                            .frame(width: 320, height: 320)
                            .padding(.top,20)
                        
                        if let leagueName = authViewModel.user?.league.name {
                            Text(leagueName)
                                .foregroundColor(Color(hex: "\(authViewModel.user?.league.color ?? "Background")"))
                                .font(.system(size: 22))
                                .fontWeight(.bold)
                        }
                        
                        Spacer()
                        
                        
                        ActionButtons(actions: authViewModel.user?.actions ?? [] )
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

struct CircularProgressView: View {
    let progress: Double
    let color: Color
    
    var body: some View {
        ZStack {
            Circle()
                .stroke(
                    color.opacity(0),
                    lineWidth: 24
                )
            Circle()
                .trim(from: 0, to: progress)
                .stroke(
                    color,
                    style: StrokeStyle(
                        lineWidth: 24,
                        lineCap: .round
                    )
                )
                .rotationEffect(.degrees(-90))
                .animation(.easeOut, value: progress)
        }
    }
}


struct ProfilePage_Previews: PreviewProvider {
    static var previews: some View {
        ProfilePage()
    }
}
