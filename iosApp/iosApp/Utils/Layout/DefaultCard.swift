//
//  DefaultCard.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct DefaultCard<Content: View>: View {
    
    @ViewBuilder let content: Content
    let backgroundColor: Color
    let alignment: Alignment
    
    init(backgroundColor: Color = Color("Default"), alignment: Alignment = .leading, content: @escaping () -> Content) {
        self.content = content()
        self.backgroundColor = backgroundColor
        self.alignment = alignment
    }
    
    var body: some View {
        VStack {
            content
        }
        .frame(maxWidth: .infinity, alignment: alignment)
        .padding(.horizontal, 16)
        .padding(.vertical, 26)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .foregroundColor(backgroundColor)
        )
        .background(Color.clear)
    }
}

struct DefaultCard_Previews: PreviewProvider {
    static var previews: some View {
        DefaultCard {
            Text("Test")
        }
    }
}
