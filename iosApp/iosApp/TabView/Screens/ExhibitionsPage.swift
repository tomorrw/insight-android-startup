//
//  ExhibitionsPage.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMPNativeCoroutinesAsync

struct ExhibitionsPage: View {
    var body: some View {
        NavigationPages() {
            VStack(alignment: .leading) {
                Text("Exhibitions")
                    .font(.system(size: 24, weight: .bold))
                    .padding(.top, 8)
                Text("Explore, Connect, Discover.")
                    .foregroundColor(Color("Secondary"))
                    .padding(.bottom, 8)
                
                VStack(alignment: .leading, spacing: 16) {
                    NavigateTo(destination: {NavigationLazyView(CompaniesByMapPage())}, label: {
                        DefaultCard(backgroundColor: Color("Primary")) {
                            DefaultCardBody (
                                title: "Exhibitions Map",
                                image: "map",
                                description: "Get to know the Floor map of our event",
                                isHighlighted: true
                            )
                        }
                    })
                    NavigateTo(destination: {NavigationLazyView(CompaniesPage())}, label: {
                        DefaultCard() {
                            DefaultCardBody (
                                title: "Companies",
                                image: "office-building",
                                description: "Get to know the companies at the heart of our event"
                            )
                        }
                    })
                    
                    NavigateTo(destination: {NavigationLazyView(CategoriesPage())}, label: {
                        DefaultCard {
                            DefaultCardBody(
                                title: "Product Categories",
                                image: "application",
                                description: "Explore the diversity of products at our conference"
                            )
                        }
                    })
                    
                    NavigateTo(destination: {NavigationLazyView(OffersPage())}, label: {
                        DefaultCard {
                            DefaultCardBody(
                                title: "Offers & Deals",
                                image: "offers",
                                description: "Find the best exclusive deals"
                            )
                        }
                    })
                    
                    Spacer()
                }
            }
            .padding(.horizontal)
            .frame(maxWidth: .infinity)
            .navigationBarHidden(true)
            .navigationTitle("Exhibitions")
            .background(Color("Background"))
        }
    }
}

struct ExhibitionsPage_Previews: PreviewProvider {
    static var previews: some View {
        ExhibitionsPage()
    }
}
