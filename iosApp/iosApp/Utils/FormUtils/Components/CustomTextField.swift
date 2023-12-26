//
//  CustomTextField.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct CustomTextField: View {
    @FocusState var isFocused: Bool
    @Binding var textValue: String
    var icon: Image
    var isSecure: Bool = false
    var isTextBox: Bool = false
    var placeholderText: String
    var showPlaceholder: Bool
    var hasError: Bool
    var hasSuccess: Bool = false
    var error: String
    
    var body: some View {
        Group {
            if isSecure {
                SecureField("", text: $textValue)
            } else if isTextBox {
                TextView(text: $textValue)
            } else {
                TextField("", text: $textValue)
            }
        }
        .accentColor(Color("HighlightPrimary"))
        .focused($isFocused)
        .textFieldLayout(icon: icon, placeholder: placeholderText, showPlaceholder: showPlaceholder, hasError: hasError, hasSuccess: hasSuccess)
        .errorFieldLayout(error: error)
        .onTapGesture {
            isFocused = true
        }
    }
}
