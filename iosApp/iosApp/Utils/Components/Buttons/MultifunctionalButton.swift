//
//  MultifunctionalButton.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct MultifunctionalButton: View {
    let action: () -> Void
    let label: String
    var textColor: Color = Color("Default")
    var backgroundColor: Color = Color("Primary")
    var borderColor: Color = Color.clear
    var isLoading: Bool = false
    
    var body: some View {
        Button {
            action()
        } label: {
            Text(label)
                .foregroundColor(textColor)
                .font(.system(size: 16))
                .frame(maxWidth: .infinity)
                .padding(16)
                .background(backgroundColor)
                .cornerRadius(16)
                .overlay {
                    if isLoading {
                        CustomLoader(strokeColor: Color("Default"))
                            .background(
                                RoundedRectangle(cornerRadius: 16)
                                    .foregroundColor(backgroundColor)
                            )
                    }
                }
        }
        .buttonStyle(.plain)
        .disabled(isLoading)
        .overlay {
            RoundedRectangle(cornerRadius: 8)
                .stroke(style: StrokeStyle(lineWidth: 1))
                .foregroundColor(borderColor)
        }

    }
}

struct MultifunctionalButton_Previews: PreviewProvider {
    static var previews: some View {
        MultifunctionalButton(action: {print("hello")}, label: "hello")
    }
}
