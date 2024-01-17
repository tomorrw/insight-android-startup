//
//  EnvironmentValues.swift
//  iosApp
//
//  Created by marcjalkh on 09/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI

extension EnvironmentValues {
    var sessionCardColors: SessionCardColors{
        get { self[SessionCardColorsKey.self] }
        set { self[SessionCardColorsKey.self] = newValue }
    }
}

struct SessionCardColorsKey: EnvironmentKey{
    typealias Value = SessionCardColors
    static let defaultValue: SessionCardColors = SessionCardColors()
}
