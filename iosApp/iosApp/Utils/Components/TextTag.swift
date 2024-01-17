//
//  TextTag.swift
//  iosApp
//
//  Created by marcjalkh on 17/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct TextTagColor{
    var textColor: Color = Color("Primary")
    var background: Color = Color("ColoredDefault")
}

struct TextTag: View {
    var text: String
    var colors: TextTagColor = TextTagColor()
    
    var body: some View {
        Text(text)
            .font(.system(size: 14))
            .foregroundColor(colors.textColor)
            .padding(5)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .foregroundColor(colors.background)
            )
    }
}

#Preview {
    TextTag(text: "Hello world!")
}
