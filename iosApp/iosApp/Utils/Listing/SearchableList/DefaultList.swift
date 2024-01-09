//
//  DefaultList.swift
//  iosApp
//
//  Created by marcjalkh on 09/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct DefaultEmptyList: View{
    @Binding var searchText : String
    @Binding var noResultText: String
    var body: some View{
        VStack(spacing: 8) {
            Image(systemName: "minus.magnifyingglass")
                .resizable()
                .frame(width: 40, height: 40)
            
            Text("No result for \"\(searchText)\"")
                .font(.custom("ReadexPro-Medium", size: 25))
            
            Text(noResultText)
                .font(.custom("ReadexPro-Medium", size: 16))
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
        }
    }
}

struct DefaultListLoader: View{
    var body: some View{
        List(0..<5) {item in
            HStack(spacing: 12) {
                Circle()
                    .fill(.gray.opacity(0.3))
                    .frame(width: 50, height: 50)
                VStack(alignment: .leading, spacing: 12) {
                    RoundedRectangle(cornerRadius: 8)
                        .fill(.gray.opacity(0.3))
                        .frame(width: 100, height: 22)
                    RoundedRectangle(cornerRadius: 16)
                        .fill(.gray.opacity(0.3))
                        .frame(maxWidth: .infinity)
                        .frame(height: 22)
                }
            }
            .padding(.vertical, 8)
            .listStyle(.plain)
            .listRowBackground(Color("Background"))
        }
    }
}

struct DefaultListItem: View{
    var item: SearchItem
    var body: some View{
        HStack(spacing: 12) {
            if item.image != nil {
                UrlImageView(urlString: item.image)
                    .scaledToFill()
                    .clipShape(Circle())
                    .frame(width: 50, height: 50)
            }
            
            VStack(alignment: .leading) {
                Text(item.title)
                    .font(.system(size: 18))
                    .padding(.bottom, 2)
                
                Text(item.description)
                    .foregroundColor(Color("Secondary"))
                    .lineLimit(1)
                    .font(.system(size: 14))
            }
        }
        .padding(.vertical, 8)
    }
}
