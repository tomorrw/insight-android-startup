//
//  MyQrPage.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//
import SwiftUI
import UiComponents

struct AnotherPage: View {
    var body: some View {
        NavigationPages() {
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
                Spacer()
            }
            .padding(.horizontal)
            .frame(maxWidth: .infinity)
            .navigationBarHidden(true)
            .navigationTitle("Home")
            .background(Color("Background"))
        }
    }
}

struct MyQrPage_Previews: PreviewProvider {
    static var previews: some View {
        AnotherPage()
    }
}
