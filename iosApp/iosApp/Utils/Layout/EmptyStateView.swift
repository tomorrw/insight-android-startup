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
    let buttonText: String?
    let buttonAction: () -> Void
    
    init(title: String, text: String, buttonText: String? = nil, buttonAction: @escaping () -> Void = {}) {
        self.title = title
        self.text = text
        self.buttonText = buttonText
        self.buttonAction = buttonAction
    }
    var body: some View {
        VStack(spacing: 14) {
            Spacer()
            
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
            if let btnText = buttonText {
                MultifunctionalButton(
                    action: {
                        buttonAction()
                    },
                    label: btnText
                )
            }
            Spacer()
        }
        .padding(.bottom, 1)
        .frame(maxHeight: .infinity)
    }
}
