//
//  TextFieldLayout.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

extension View {
    func textFieldLayout(icon: Image, placeholder: String, showPlaceholder: Bool, hasError: Bool, hasSuccess: Bool = false) -> some View {
        self.modifier(TextFieldLayout(icon: icon, placeholder: placeholder, showPlaceholder: showPlaceholder, hasError: hasError, hasSuccess: hasSuccess))
    }
}

struct TextFieldLayout: ViewModifier {
    var icon: Image
    var placeholder: String
    var showPlaceholder: Bool
    var hasError: Bool
    var hasSuccess: Bool
    
    func body(content: Content) -> some View {
        HStack{
            icon
                .padding(.trailing, 12)
            content
                .font(.system(size: 16))
                .padding(16)
                .background(Color.clear)
                .overlay(
                    RoundedRectangle(cornerRadius: 16)
                        .stroke(
                            hasError ? Color("Error") : hasSuccess ? Color("Success") : Color("Secondary"),
                            lineWidth: 1
                        )
                )
                .placeholder(placeholder, when: showPlaceholder)
        }
    }
}

