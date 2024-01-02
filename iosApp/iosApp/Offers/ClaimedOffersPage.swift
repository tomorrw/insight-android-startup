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
    
    var body: some View {
        if vm.isLoading {
            CustomLoader()
        }
        else {
            if vm.data.isEmpty{
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
            }
            else {
                OffersList(vm: vm)
                    .refreshable { Task{await vm.refresh() }}
                    .cornerRadius(16, corners: .topRight)
                    .cornerRadius(16, corners: .topLeft)
                    .frame(maxHeight: .infinity)
            }
        }
    }
}
