//
//  HorizontalDrag.swift
//  iosApp
//
//  Created by marcjalkh on 17/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI

func horizontalDrag(next: @escaping () -> Void, previous: @escaping () -> Void) -> some Gesture {
    DragGesture(coordinateSpace: .local)
        .onEnded { value in
            if value.translation.width < -50 {
                withAnimation { next() }
            } else if value.translation.width > 50 {
                withAnimation { previous() }
            }
        }
}
