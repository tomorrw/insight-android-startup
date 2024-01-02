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
    
    var body: some View {
        ZStack{
            VStack (spacing: 16) {
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
                    
                }).padding(.top, 20)
                    .padding(.bottom, 2)
                
                
                
                OffersList(vm: vm)
                    .refreshable { Task{await vm.refresh() }}
                    .cornerRadius(16, corners: .topRight)
                    .cornerRadius(16, corners: .topLeft)
                    .frame(maxHeight: .infinity)
            }
        }
        .padding(.horizontal)
        .frame(maxWidth: .infinity)
        .background(Color("Background"))
        .navigationTitle("Offers & Deals")
        .navigationBarTitleDisplayMode(.inline)
    }
}

