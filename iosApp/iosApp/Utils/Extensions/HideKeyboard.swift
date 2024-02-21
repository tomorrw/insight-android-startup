//
//  HideKeyboard.swift
//  iosApp
//
//  Created by Said on 18/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

extension View {
    func hideKeyboard() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}

extension ViewModifier {
    func hideKeyboard() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}

struct HideKeyboard: ViewModifier {
    @State var currentHeight: CGFloat = 0
    
    func body(content: Content) -> some View {
        content
            .gesture(
                DragGesture()
                    .onEnded({ value in
                        if value.translation.height > 50 {
                            hideKeyboard()
                        }
                    })
            )
    }
}

extension View {
    func hideKeyboard() -> some View {
        return modifier(HideKeyboard())
    }
}
