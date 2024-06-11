//
//  ClaimedOffersPage.swift
//  iosApp
//
//  Created by marcjalkh on 02/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI
import UiComponents
import SearchableList

struct ClaimedOffersPage: View {
    @StateObject var vm = ClaimedOffersPageViewModel()
    @State private var isShowingOfferAlert = false

    var body: some View {
        SearchableList(
            vm: vm,
            showSeperators: false,
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
            rowView: { item in
                NavigateTo {
                    VStack{
                        if let offerPost = item as? OfferSearchItem,
                           let postId = offerPost.postId
                        {
                            PostDetailPage(id: postId)
                        }
                        else {
                            EmptyView()
                                .onAppear{
                                    isShowingOfferAlert = true
                                }
                        }
                    }
                } label: {
                    HighlightedCard(
                        image: item.image,
                        title: item.title,
                        description: item.description,
                        cardColor: DefaultColors.defaultCardColor
                    )
                }
                
            }            
        )
        .task { await vm.getOffers() }
        .navigationTitle("Offers & Deals")
        .navigationBarTitleDisplayMode(.inline)
        .alert("Offer Not Found!", isPresented: $isShowingOfferAlert, actions: { }, message: {
            Text("This offer is currently unavailable!")
        })
    }
}
