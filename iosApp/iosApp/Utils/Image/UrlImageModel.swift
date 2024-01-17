//
//  UrlImageModel.swift
//  iosApp
//
//  Created by Yammine on 4/24/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import UIKit
import SwiftUI

class UrlImageModel: ObservableObject {
    @Published var image: UIImage?
    var urlString: String?
    var imageCache = ImageCache.getImageCache()
    var urlSession : URLSession
    
    init(urlString: String?) {
        self.urlString = urlString
        let sessionConfig = URLSessionConfiguration.default
        sessionConfig.timeoutIntervalForRequest = 30.0
        sessionConfig.timeoutIntervalForResource = 60.0
        self.urlSession = URLSession(configuration: sessionConfig)
        loadImage()
    }
    
    func loadImage() {
        if loadImageFromCache() {
            return
        }
        /*======================================================================
         This here is being called multiple times one of the sollutions found is
         save the loaded image and enter the function only if the image is not
         loaded (Not done cz found bugs in reloading and handling states!
         ======================================================================*/
        loadImageFromUrl()
    }
    
    func loadImageFromCache() -> Bool {
        guard let urlString = urlString, urlString.isEmpty == false else {
            return false
        }

        guard let cacheImage = imageCache.get(forKey: urlString) else {
            return false
        }
        if image == nil {
            image = cacheImage
        }
        return true
    }

    func getImageFromResponse(data: Data?, response: URLResponse?, error: Error?) {
        guard error == nil else {
            debugPrint("Error: \(error!)")
            return
        }
        guard let data = data else {
            debugPrint("No data found")
            return
        }
        
        DispatchQueue.main.async {
            guard let loadedImage = UIImage(data: data) else {
                return
            }
            self.imageCache.set(forKey: self.urlString!, image: loadedImage)
            if self.image == nil {
                self.image = loadedImage
            }
        }
    }
    
    func loadImageFromUrl() {
        guard let urlString = urlString, urlString.isEmpty == false else {
            return
        }
        
        guard let url = URL(string: urlString) else {
            return
        }
        let task = urlSession.dataTask(with: url, completionHandler: getImageFromResponse(data:response:error:))
        task.resume()
    }
}

class ImageCache {
    var cache = NSCache<NSString, UIImage>()

    func get(forKey: String) -> UIImage? {
        return cache.object(forKey: NSString(string: forKey))
    }

    func set(forKey: String, image: UIImage) {
        cache.setObject(image, forKey: NSString(string: forKey))
    }
}

extension ImageCache {
    private static var imageCache = ImageCache()
    static func getImageCache() -> ImageCache {
        return imageCache
    }
}

