//
//  EmptyStateView.swift
//  iosApp
//
//  Created by Said on 17/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct EmptyStateView: View {
    let title: String
    let text: String
    let buttonText: String
    let buttonAction: () -> Void
    
    var body: some View {
        VStack(spacing: 14) {
            Image(systemName: "exclamationmark.circle")
                .font(.system(size: 45, weight: .semibold))
            
            Text(title)
                .font(.system(.title))
                .multilineTextAlignment(.center)
            
            Text(text)
                .font(.system(.subheadline))
                .multilineTextAlignment(.center)
                .foregroundColor(Color("Secondary"))
                .lineSpacing(5)
                .padding(.bottom, 6)
            
            MultifunctionalButton(
                action: {
                    buttonAction()
                },
                label: buttonText
            )
            
            Spacer()
        }
        .padding(.top, 25)
        .frame(maxHeight: .infinity)
    }
}
