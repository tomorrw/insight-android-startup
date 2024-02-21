//
//  EmptyStateNavLink.swift
//  iosApp
//
//  Created by Yammine on 5/23/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct EmptyStateNavLink<Content: View>: View {
    let title: String
    let text: String
    let destinationView: Content
    let navLinkText: String
    
    var body: some View {
        VStack(spacing: 14) {
            Spacer()
            Image(systemName: "exclamationmark.circle")
                .font(.system(size: 45, weight: .semibold))
            
            Text(title)
                .font(.system(.title))
                .multilineTextAlignment(.center)
            
            Text(text)
                .font(.system(.subheadline))
                .multilineTextAlignment(.center)
                .foregroundColor(Color("Secondary"))
                .lineSpacing(5)
                .padding(.bottom, 6)
            
            NavigateTo {
                destinationView
            } label: {
                Text(navLinkText)
                    .foregroundColor(Color("Default"))
                    .font(.system(size: 16))
                    .frame(maxWidth: .infinity)
                    .padding(16)
                    .background(Color("Primary"))
                    .cornerRadius(16)
            }

            Spacer()
        }
        .padding(.bottom, 1)
        .frame(maxHeight: .infinity)
    }
}

//struct EmptyStateNavLink_Previews: PreviewProvider {
//    static var previews: some View {
//        EmptyStateNavLink()
//    }
//}
