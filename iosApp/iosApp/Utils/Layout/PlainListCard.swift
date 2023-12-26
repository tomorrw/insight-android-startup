//
//  PlainListCard.swift
//  iosApp
//
//  Created by marcjalkh on 27/09/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct PlainListCard: View {
    let image: String?
    let title: String
    let description: String
    
    var body: some View {
        HStack(spacing: 12) {
            if image != nil {
                UrlImageView(urlString: image)
                    .scaledToFill()
                    .clipShape(Circle())
                    .frame(width: 50, height: 50)
                    .padding(.vertical, 8)
            }
            VStack{
                HStack{
                    VStack(alignment: .leading) {
                        Text(title)
                            .font(.system(size: 18))
                            .padding(.bottom, 2)
                        
                        Text(description)
                            .foregroundColor(Color("Secondary"))
                            .lineLimit(1)
                            .font(.system(size: 14))
                    }
                    Spacer()
                    Image(systemName: "chevron.right")
                }
                .padding(.vertical, 8)
                Divider()
            }
        }
    }
}

struct PlainListCard_Previews: PreviewProvider {
    static var previews: some View {
        PlainListCard(image: "",title: "",description: "")
    }
}
