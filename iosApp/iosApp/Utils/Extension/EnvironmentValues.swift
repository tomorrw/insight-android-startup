//
//  EnvironmentValues.swift
//  iosApp
//
//  Created by marcjalkh on 09/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI

extension EnvironmentValues {
    var sessionCardBackgroundColor: Color {
        get { self[BackgroundColorKey.self] }
        set { self[BackgroundColorKey.self] = newValue }
    }
}

struct BackgroundColorKey: EnvironmentKey {
    static let defaultValue: Color = Color("Default")
}
