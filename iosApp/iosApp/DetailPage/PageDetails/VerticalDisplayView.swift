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
    @Binding var pages: [PagePresentationModel]
    @Binding var actions: [Action]
    
    var body: some View{
        VStack {
            if !pages.isEmpty {
                Divider().padding(.bottom, 5)
            }
            LazyVStack(alignment: .leading, spacing: 32) {
                ForEach(pages) { page in
                    ForEach(page.sections) { section in
                        SectionDisplayPage(section: section)
                    }
                }
            }
            
            ActionButtons(actions: actions)
        }
        .padding(.horizontal)
        .padding(.bottom, 30)
    }
}
