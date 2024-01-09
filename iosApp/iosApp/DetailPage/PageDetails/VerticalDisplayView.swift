//
//  VerticalDisplayView.swift
//  iosApp
//
//  Created by marcjalkh on 09/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared

struct VerticalDisplayView: View{
    @Binding var sections: [SectionDisplayInfo]
    @Binding var actions: [Action]
    
    var body: some View{
        VStack {
            Divider().padding(.bottom, 5)
            
            LazyVStack(alignment: .leading, spacing: 32) {
                ForEach(sections) { section in
                    SectionDisplayPage(section: section)
                }
            }
            
            ActionButtons(actions: actions)
        }
        .padding(.horizontal)
        .padding(.bottom, 30)
    }
}
