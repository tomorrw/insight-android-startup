//
//  ErrorFieldLayout.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

extension View {
    func errorFieldLayout(error: String) -> some View {
        self.modifier(ErrorFieldLayout(error: error))
    }
}

struct ErrorFieldLayout: ViewModifier {
    var error: String?
    
    func body(content: Content) -> some View {
        content
            .overlay (
                VStack {
                    Text(error ?? "")
                        .font(.system(size: 12, weight: .medium))
                        .padding(.horizontal, 8)
                        .foregroundColor(Color("Error"))
                        .lineLimit(1)
                        .offset(y: 16)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottomLeading)
            )
    }
}
