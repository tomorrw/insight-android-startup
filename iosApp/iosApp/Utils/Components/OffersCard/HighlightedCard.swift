//
//  OffersCard.swift
//  iosApp
//
//  Created by marcjalkh on 20/09/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct HighlightedCard: View {
    let image: String?
    let title: String
    let description: String
    
    var body: some View {
        HStack{
            UrlImageView(urlString: image ?? "" )
                .clipShape(Circle())
                .frame(width: 80, height: 80, alignment: .leading)
            
            VStack ( alignment: .leading ){
                
                Text(title)
                    .font(.system(size: 18))
                    .multilineTextAlignment(.leading)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                Text(description)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .foregroundColor(Color("Secondary"))
            }
            .frame(maxWidth: .infinity)
            .padding(.horizontal, 5)
        }
        .frame(maxWidth: .infinity)
        .padding(15)
        .background(Color("Default"))
        .cornerRadius(16)
    }
}

struct HighlightedCard_Previews: PreviewProvider {
    static var previews: some View {
        HighlightedCard(image: "", title: "", description: "")
    }
}
