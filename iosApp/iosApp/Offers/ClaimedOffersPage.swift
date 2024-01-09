//
//  ClaimedOffersPage.swift
//  iosApp
//
//  Created by marcjalkh on 02/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct ClaimedOffersPage: View {
    @StateObject var vm = ClaimedOffersPageViewModel()
    @State private var hasNoPostId = false

    var body: some View {
        SearchableList(
            vm: vm,
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
            emptyListView: {
                VStack(spacing: 14) {
                    Image(systemName: "exclamationmark.circle")
                        .font(.system(size: 45, weight: .semibold))
                    
                    Text("No Claimed Offers")
                        .font(.system(.title))
                        .multilineTextAlignment(.center)
                    
                    Text("It seems you haven't claimed any offers yet. Don't miss out on great deals and discounts! Start exploring now!")
                        .font(.system(.subheadline))
                        .multilineTextAlignment(.center)
                        .foregroundColor(Color("Secondary"))
                        .lineSpacing(5)
                        .padding(.bottom, 6)
                }
                .padding()
            },
            listView: { item in
                HighlightedCard(image: item.image, title: item.title, description: item.description)
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
