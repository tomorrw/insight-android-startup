//
//  TextButton.swift
//  iosApp
//
//  Created by Said on 14/09/2022.
//  Copyright © 2022 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct TextButton: View {
    let action: () -> Void
    let label: String
    var image: String? = nil
    var textColor: Color = Color("HighlightPrimary")
    var backgroundColor: Color = .white.opacity(0)
    var borderColor: Color = .white.opacity(0)
    var isLoading: Bool = false
    
    var body: some View {
        Button {
            action()
        } label: {
            HStack {
                if !isLoading, let image = image {
                    Image(systemName: image)
                }
                Text(isLoading ? "" : label)
            }
            .accentColor(textColor)
            .foregroundColor(textColor)
            .font(.system(size: 16))
            .frame(maxWidth: .infinity)
            .padding(.horizontal, 13)
            .padding(.vertical, 6)
            .background(backgroundColor)
            .cornerRadius(8)
            .overlay {
                if isLoading {
                    CustomLoader(strokeColor: textColor)
                        .background(
                            RoundedRectangle(cornerRadius: 16)
                                .foregroundColor(backgroundColor)
                        )
                }
            }
        }
        .overlay {
            RoundedRectangle(cornerRadius: 16)
                .stroke(style: StrokeStyle(lineWidth: 1))
                .foregroundColor(borderColor)
        }

    }
}

struct TextButton_Previews: PreviewProvider {
    static var previews: some View {
        TextButton(action: {print("hello")}, label: "hello")
    }
}
