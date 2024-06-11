//
//  LecturesPage.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import UiComponents

struct LecturesPage: View {
    var body: some View {
        NavigationPages() {
            
            VStack(alignment: .leading) {
                Text("Lectures")
                    .font(.system(size: 24, weight: .bold))
                    .padding(.top, 8)
                Text("Explore lectures and plan your day")
                    .foregroundColor(Color("Secondary"))
                    .padding(.bottom, 8)
                
                VStack(alignment: .leading, spacing: 16) {
                    NavigateTo {
                        SessionsPage()
                            .navigationTitle("Lectures Schedule")
                            .navigationBarTitleDisplayMode(.inline)
                    } label: {
                        DefaultCard(backgroundColor: Color("Primary")) {
                            DefaultCardBody(
                                title: "Lectures Schedule",
                                image: "schedule",
                                description: "Explore the complete schedule of lectures by day",
                                cardColors: CardColors(
                                    foreground: Color("Background"),
                                    secondaryText: Color("Secondary")
                                )
                            )
                        }
                    }
                    
                    NavigateTo {
                        MySessionsPage()
                            .navigationTitle("My Lectures")
                            .navigationBarTitleDisplayMode(.inline)
                    } label: {
                        DefaultCard(backgroundColor: Color("Default")) {
                            DefaultCardBody(
                                title: "My Lectures",
                                image: "bookmark",
                                description: "Manage and access your bookmarked lectures",
                                cardColors: DefaultColors.defaultCardBodyColor
                            )
                        }
                    }
                    
                    NavigateTo {
                        NavigationLazyView(MyProgressPage())
                    } label: {
                        DefaultCard(backgroundColor: Color("Default")) {
                            DefaultCardBody(
                                title: "My Progress",
                                image: "progress",
                                description: "Discover our diverse speakers",
                                cardColors: DefaultColors.defaultCardBodyColor
                            )
                        }
                    }
                    
                    NavigateTo {
                        NavigationLazyView(SpeakersPage())
                            .navigationTitle("Speakers")
                            .navigationBarTitleDisplayMode(.inline)
                    } label: {
                        DefaultCard(backgroundColor: Color("Default")) {
                            DefaultCardBody(
                                title: "Speakers",
                                image: "speaker",
                                description: "Discover our diverse speakers",
                                cardColors: DefaultColors.defaultCardBodyColor
                            )
                        }
                    }
                    Spacer()
                }
                .frame(maxWidth: .infinity)
            }
            .navigationTitle("Lectures")
            .navigationBarHidden(true)
            .padding(.horizontal)
            .background(Color("Background"))
        }
    }
}

struct LecturesPage_Previews: PreviewProvider {
    static var previews: some View {
        LecturesPage()
    }
}
