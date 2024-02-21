//
//  coerceIn.swift
//  iosApp
//
//  Created by Yammine on 4/21/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation

extension FloatingPoint {
    func coerceIn(_ minValue: Self, _ maxValue: Self) -> Self {
        if self > maxValue { return maxValue }
        if self < minValue { return minValue }
        return self
    }
}
