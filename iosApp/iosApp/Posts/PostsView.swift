//
//  PostView.swift
//  iosApp
//
//  Created by Yammine on 4/28/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import UiComponents
import ImageCaching

struct PostsView: View {
    let posts: [Post]
    
    var body: some View {
        LazyVStack(alignment: .leading, spacing: 16) {
            ForEach(posts.indices, id: \.self) { i in
                let post = posts[i]
                
                NavigateTo {
                    PostDetailPage(id: post.id)
                } label: {
                    VStack(alignment: .leading) {
                        if let image = post.image {
                            UrlImageView(urlString: image)
                                .aspectRatio(contentMode: .fit)
                                .clipShape(RoundedRectangle(cornerRadius: 16))
                                .padding(.bottom, 5)
                        }
                        
                        Text("\(post.title)")
                            .font(.system(size: 20))
                            .padding(.bottom)
                        
                        HStack {
                            Capsule()
                                .fill(Color("HighlightPrimary"))
                                .frame(width: 72, height: 2)
                            
                            Spacer()
                        }
                        .frame(maxWidth: .infinity)
                        .overlay(alignment: .leading) {
                            Capsule()
                                .fill(Color("HighlightPrimary"))
                                .frame(width: 72, height: 2)
                        }
                        .padding(.bottom, 4)
                        
                        Text("\(post.description_)").foregroundColor(Color("Secondary"))
                    }
                    .padding(.bottom, 5)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color("Default"))
                    .clipShape(RoundedRectangle(cornerRadius: 16))
                }
                .buttonStyle(FlatLinkStyle()) 
            }
        }
    }
}

struct PostView_Previews: PreviewProvider {
    static var previews: some View {
        PostsView(posts: [])
    }
}
