//
//  UrlImageView.swift
//  iosApp
//
//  Created by Yammine on 4/24/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import UIKit

struct UrlImageView: View {
    @ObservedObject var urlImageModel: UrlImageModel
    let placeholderImage: String
    
    init(urlString: String?, placeholderImage: String = "placeholderImage") {
        urlImageModel = UrlImageModel(urlString: urlString)
        self.placeholderImage = placeholderImage
    }
    
    var body: some View {
        if urlImageModel.image != nil {
            Image(uiImage: urlImageModel.image!)
                .resizable()
        } else {
            Image(placeholderImage)
                .resizable()
        }
    }
}
