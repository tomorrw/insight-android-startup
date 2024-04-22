//
//  Tag.swift
//  iosApp
//
//  Created by Yammine on 22/04/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct Tag {
    let text: String
    let background: Color
    let color: Color
    
    init(tag: shared.Session.Tag) {
        self.text = tag.text
        self.background = Color(hex: "\(tag.background.replacingOccurrences(of: "#", with: ""))")
        self.color = Color(hex: "\(tag.color.replacingOccurrences(of: "#", with: ""))")
    }
}
