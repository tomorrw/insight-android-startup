//
//  OffersList.swift
//  iosApp
//
//  Created by marcjalkh on 29/09/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared

struct OffersList<Content: View>: View {
    @ObservedObject var vm: OffersPageViewModel
    @State private var hasNoPostId = false
    @State private var isDisplayingError = false
    @ViewBuilder var emptyView: () -> Content
    
    init(vm: OffersPageViewModel, emptyView: @escaping () -> Content = { Text("No offers found") } ) {
        self.vm = vm
        self.emptyView = emptyView
    }
    
    var body: some View {
        ZStack{
            if ( vm.isLoading && vm.data.isEmpty) {
                VStack(spacing: 20) {
                    ForEach(0..<2, id:\.self) { _ in
                        RoundedRectangle(cornerRadius: 16)
                            .fill(.gray.opacity(0.3))
                            .frame(maxWidth: .infinity)
                            .frame(height: 75)
                    }
                    
                    Spacer()
                }
                .frame(maxHeight: .infinity)
                .padding(.vertical, 24)
            }
            
            
            ScrollView{
                if (vm.data.isEmpty && !vm.isLoading) {
                    VStack{
                        emptyView()
                            .padding()
                            .frame(maxWidth: .infinity)
                    }
                }
                else {
                    ForEach (vm.data , id: \.self){offer in
                        if offer.postId != nil {
                            NavigateTo(destination: {
                                PostDetailPage(id: offer.postId!)
                            }, label: {
                                HighlightedCard(image: offer.image, title: offer.title, description: offer.company.title)
                            })
                        }
                        else{
                            Button {
                                hasNoPostId = true
                            } label: {
                                HighlightedCard(image: offer.image, title: offer.title, description: offer.company.title)
                            }
                            
                        }
                        
                    }
                }
            }
            .alert("Offer Not Found!", isPresented: $hasNoPostId, actions: { }, message: {
                Text("This offer is currently unavailable!")
            })
        }
        .task { await vm.getOffers() }
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
    }
}
