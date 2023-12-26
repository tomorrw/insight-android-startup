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
    @StateObject var urlImageModel: UrlImageModel
    let placeholderImage: String
    
    init(urlString: String?, placeholderImage: String = "placeholderImage") {
        _urlImageModel = StateObject(wrappedValue: UrlImageModel(urlString: urlString))
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
