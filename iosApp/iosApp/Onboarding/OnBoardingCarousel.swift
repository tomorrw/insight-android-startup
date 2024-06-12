//
//  OnBoardingCarousel.swift
//  iosApp
//
//  Created by Yammine on 4/19/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import UiComponents

struct OnBoardingCarousel: View {
    let goToSignUp: () -> Void

    var body: some View {
        VStack {
            Spacer()
            
            Text("Hello OnBoarding")
            MultifunctionalButton(
                action: goToSignUp,
                label: "Get Started",
                colors: DefaultColors.buttonColor
            )
            
            Spacer()
        }
        .frame(maxHeight: .infinity)
    }
}

