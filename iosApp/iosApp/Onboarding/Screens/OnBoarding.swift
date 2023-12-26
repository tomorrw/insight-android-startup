//
//  OnBoarding.swift
//  iosApp
//
//  Created by Yammine on 4/19/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct OnBoarding: View {
    @State private var isAuthScreenVisible: Bool = false
    
    var body: some View {
        if isAuthScreenVisible { // isAuthenticated is for guests that wish to sign up
            AuthenticationView()
        } else {
            OnBoardingCarousel(goToSignUp: { isAuthScreenVisible = true})
        }
    }
}

struct OnBoarding_Previews: PreviewProvider {
    static var previews: some View {
        OnBoarding()
    }
}
