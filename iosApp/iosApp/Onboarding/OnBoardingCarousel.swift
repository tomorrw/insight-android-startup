//
//  OnBoardingCarousel.swift
//  iosApp
//
//  Created by Yammine on 4/19/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Resolver
import shared



struct OnBoardingInfo: Identifiable {
    var id: UUID = UUID()
    var title: String
    var subtitle: String
}

struct OnBoardingCarousel: View {
    let goToSignUp: () -> Void
    var horizontalSpacing: CGFloat = 30
    var infoArray: [OnBoardingInfo] = [
        OnBoardingInfo(
            title: "Discover and take part in our specialized Lectures 📚",
            subtitle: "Get involved in our conference sessions by registering your attendance and participating in Q&A. Get to know our guest speakers and get notified of their lectures."
        ),
        OnBoardingInfo(
            title: "Collaborate & Connect with leading dental companies 🌐",
            subtitle: "Develop connections with our exhibition sponsors through posts & updates and elevate your dental practice with once-in-a-lifetime offers & promotions."
        ),
        OnBoardingInfo(
            title: "Stay in the loop with our latest updates 🔔",
            subtitle: "Sign up for your preferred lectures and turn on our notifications to get reminded of your custom schedule; the entire conference in the palm of your hand!"
        ),
    ]
    @State var backgroundOffset: CGFloat = 0
    
    var body: some View {
        VStack {
            
            VStack(spacing: 20) {
                GeometryReader { geo in
                    HStack(spacing: horizontalSpacing) {
                        ForEach(infoArray) { info in
                            VStack(spacing: 8) {
                                Text(info.title)
                                    .font(.system(size: 24, weight: .medium))
                                    .multilineTextAlignment(.center)
                                Text(info.subtitle)
                                    .font(.system(size: 16, weight: .light))
                                    .multilineTextAlignment(.center)
                            }
                            .padding(0)
                            .frame(width: geo.size.width)
                        }
                    }
                    .swipePages(
                        backgroundOffset: $backgroundOffset,
                        offsetWidth: geo.size.width + horizontalSpacing,
                        pagesCount: infoArray.count,
                        timed: false
                    )
                }
                .frame(maxWidth: .infinity)
                
                PageIndicator(
                    pagesCount: infoArray.count,
                    backgroundOffset: backgroundOffset
                )
            }
            .frame(height: 180)
            
            Spacer()
                .frame(height: 32)
            
            VStack {
                if Int(backgroundOffset) == infoArray.count - 1 {
                    MultifunctionalButton(action: goToSignUp, label: "Get Started")
                } else {
                    MultifunctionalButton(
                        action: {
                            withAnimation {
                                backgroundOffset += 1
                            }
                        }
                        , label: "Next"
                    )
                }
            }
            .frame(height: 50)
        }
        .padding(.horizontal, 24)
        .frame(maxHeight: .infinity)
    }
}

struct OnBoardingCarousel_Previews: PreviewProvider {
    static var previews: some View {
        OnBoardingCarousel(goToSignUp: {})
    }
}
