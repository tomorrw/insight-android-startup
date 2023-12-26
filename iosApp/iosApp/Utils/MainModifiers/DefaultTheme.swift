//
//  DefaultTheme.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct DefaultTheme: ViewModifier {
     
    func body(content: Content) -> some View {
        return VStack {
            content
        }
        .font(.system(size: 16))
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .foregroundColor(Color("Primary"))
        .background(Color("Background"))
        .tint(Color("Primary"))
        .accentColor(Color("Background"))
        .preferredColorScheme(.light)
        .environment(\.colorScheme, .light)
    }
}

extension View {
    func defaultTheme() -> some View {
        return modifier(DefaultTheme())
    }
}
