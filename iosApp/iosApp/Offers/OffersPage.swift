//
//  OffersPage.swift
//  iosApp
//
//  Created by marcjalkh on 20/09/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import UiComponents
import SearchableList

struct OffersPage: View {
    @StateObject var vm: OffersPageViewModel = OffersPageViewModel()
    @State private var isShowingOfferAlert = false
    
    var body: some View {
        SearchableList(
            vm: vm,
            isSearchable: false,
            showSeperators: false,
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
                    .background(Color("Surface"))
                    .cornerRadius(16)
                })
                .padding()
                .background(Color("Background"))
            }
        )
        .task { await vm.getOffers() }
        .navigationTitle("Offers & Deals")
        .navigationBarTitleDisplayMode(.inline)
        .alert("Offer Not Found!", isPresented: $isShowingOfferAlert, actions: { }, message: {
            Text("This offer is currently unavailable!")
        })
        .background(Color("Background"))
    }
}

