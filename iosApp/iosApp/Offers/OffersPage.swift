//
//  OffersPage.swift
//  iosApp
//
//  Created by marcjalkh on 20/09/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct OffersPage: View {
    @StateObject var vm: OffersPageViewModel = OffersPageViewModel()
    @State private var hasNoPostId = false
    
    var body: some View {
        SearchableList(
            vm: vm,
            isSearchable: false,
            showSeperators: false,
            itemDetailPage: { item in
                VStack{
                    if let postId = vm.getPostId(id: item.id) {
                        PostDetailPage(id: postId)
                    }
                    else {
                        EmptyView()
                            .onAppear{
                                hasNoPostId = true
                            }
                    }
                }},
            listView: { item in
                HighlightedCard(image: item.image, title: item.title, description: item.description)
            },
            customHeader: {
                
                NavigateTo(destination: {
                    ClaimedOffersPage()
                }, label: {
                    HStack{
                        Text("Claimed")
                        Spacer()
                        Image(systemName: "chevron.right")
                    }
                    .padding(16)
                    .background(Color("Dark"))
                    .cornerRadius(16)
                })
                .padding()
                .background(Color("Background"))
            }
        )
        .task { await vm.getOffers() }
        .navigationTitle("Offers & Deals")
        .navigationBarTitleDisplayMode(.inline)
        .alert("Offer Not Found!", isPresented: $hasNoPostId, actions: { }, message: {
            Text("This offer is currently unavailable!")
        })
    }
}

