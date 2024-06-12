//
//  HomePage.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import Resolver
import UiComponents

struct HomePage: View {
    @InjectedObject var authViewModel: AuthenticationViewModel

    var body: some View {
        NavigationPages(content: {
            VStack(alignment: .leading){
                HStack {
                    VStack(alignment: .leading) {
                        Text("Home")
                            .font(.system(size: 24, weight: .bold))
                        Text("Welcome back")
                            .foregroundColor(Color("Secondary"))
                    }
                    Spacer()
                }
                .padding(.top, 8)
                
                MultifunctionalButton(
                    action: {
                        authViewModel.logout()
                    },
                    label: "Logout",
                    colors: DefaultColors.buttonColor
                )
                
                Spacer()
            }
            .padding(.horizontal)
            .frame(maxWidth: .infinity)
            .navigationBarHidden(true)
            .navigationTitle("Home")
            .background(Color("Background"))
        })
    }
}

struct HomePage_Previews: PreviewProvider {
    static var previews: some View {
        HomePage()
    }
}
