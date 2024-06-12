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
    var body: some View {
        NavigationPages(content: {
            PageHeader("Home", background: Color("Background")) {
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
            } content: {
                Spacer()
            }
        })
    }
}

struct HomePage_Previews: PreviewProvider {
    static var previews: some View {
        HomePage()
    }
}
