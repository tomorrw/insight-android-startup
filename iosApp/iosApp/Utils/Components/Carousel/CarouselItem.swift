//
//  CarouselItem.swift
//  iosApp
//
//  Created by Said on 16/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct CarouselItem: View {
    @Environment(\.openURL) private var openURL
    let item: CarouselItemModel
    let width: CGFloat
    
    var body: some View {
        Button {
            if let url = URL(string: item.ctaUrl) {
                openURL(url)
            }
        } label: {
            UrlImageView(urlString: item.imageUrl)
                .scaledToFill()
//                .frame(maxWidth: .infinity)
                .frame(
                    width: width,
                    height: CarouselItemDimensions.shared.normalDimensions.height
                )
                .cornerRadius(16)
        }
    }
}

struct CarouselItem_Previews: PreviewProvider {
    
    static var previews: some View {
        CarouselItem(
            item: CarouselItemModel(
                imageUrl: "https://image-community-test.belight.tv/images/post/1908771e6b254697b0d282398e0af2cb-zmRIj.jpg",
                ctaUrl: "https://google.com"
            ), width: 60
        )
    }
}

struct CarouselItemModel: Hashable {
    let imageUrl: String?
    let ctaUrl: String
}

struct CarouselItemDimensions {
    let normalDimensions = CGSize(width: UIScreen.main.bounds.width, height: 0.30 * UIScreen.main.bounds.width)
    
    static let shared = CarouselItemDimensions()
}
