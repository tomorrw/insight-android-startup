//
//  FormHiddenBackground.swift
//  iosApp
//
//  Created by marcjalkh on 14/02/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import Foundation
import SwiftUI

struct FormHiddenBackground: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content.scrollContentBackground(.hidden)
        } else {
            content.onAppear {
                UITableView.appearance().backgroundColor = .clear
            }
            .onDisappear {
                UITableView.appearance().backgroundColor = .systemGroupedBackground
            }
        }
    }
}
