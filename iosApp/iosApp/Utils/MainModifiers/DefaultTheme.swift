//
//  DefaultTheme.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct DefaultTheme: ViewModifier {
    @Environment(\.colorScheme) var colorScheme
    @AppStorage("selectedColorTheme") private var selectedColorTheme = "Auto"

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
        .preferredColorScheme(decideThemeColor())
        
    }
    private func setupColorScheme(_ colorscheme: UIUserInterfaceStyle) {
        UIApplication.shared.connectedScenes.forEach { scene in
            guard let scene = scene as? UIWindowScene else { return }
            scene.windows.forEach { $0.overrideUserInterfaceStyle = colorscheme }
        }
    }
    
    private func decideThemeColor() -> ColorScheme? {
        switch selectedColorTheme {
        case "Auto":
            setupColorScheme(.unspecified)
            return nil
        case "Light":
            setupColorScheme(.light)
            return .light
        case "Dark":
            setupColorScheme(.dark)
            return .dark
        default:
            return nil
        }
    }
}

extension View {
    func defaultTheme() -> some View {
        return modifier(DefaultTheme())
    }
}
