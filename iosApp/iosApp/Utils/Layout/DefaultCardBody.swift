//
//  DefaultCardBody.swift
//  iosApp
//
//  Created by Yammine on 4/20/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct DefaultCardBody: View {
    let title: String
    let image: String
    let description: String
    var isHighlighted: Bool = false
    var body: some View {
        HStack(alignment: .center, spacing: 16) {
            Image(uiImage: UIImage(named: image)!)
                .resizable()
                .frame(width: 48, height: 48)
            
            VStack(alignment: .leading, spacing: 4) {
                Text(title)
                    .foregroundColor(isHighlighted ? Color("Background") : Color("Primary"))
                    .font(.system(size: 18, weight: .medium))
                Text(description)
                    .foregroundColor(Color("Secondary"))
            }
            .multilineTextAlignment(.leading)
        }
    }
}

struct DefaultCardBody_Previews: PreviewProvider {
    static var previews: some View {
        DefaultCardBody(title: "Hey", image: "tribune", description: "hello")
    }
}
