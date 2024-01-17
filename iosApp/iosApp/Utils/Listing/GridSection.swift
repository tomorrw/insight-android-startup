//
//  GridSection.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct GridSection<Data: RandomAccessCollection, Content: View>: View where Data.Element: Hashable {
    @ViewBuilder var content: (Data.Element) -> Content
    private var gridColumns: [GridItem]
    private let verticalSpacing: CGFloat
    private let horizontalPadding: CGFloat
    var data: Data
    
    init(
        _ data: Data,
        gridColumns: Int = 2,
        horizontalPadding: CGFloat = 28,
        verticalSpacing: CGFloat = 18,
        @ViewBuilder content: @escaping (Data.Element) -> Content
    ) {
        self.data = data
        self.content = content
        self.horizontalPadding = horizontalPadding
        self.verticalSpacing = verticalSpacing
        if gridColumns <= 3 {
            self.gridColumns = []
            for _ in 1...gridColumns {
                self.gridColumns.append(GridItem(.flexible()))
            }
        } else {
            print("Grid component only supports between 1 and 3 columns.")
            self.gridColumns = [GridItem(.flexible())]
        }
    }
    
    var body: some View {
        LazyVGrid(columns: gridColumns, spacing: verticalSpacing) {
            ForEach(data, id: \.self) { item in
                content(item)
            }
        }
        .padding(.horizontal, horizontalPadding)
        .padding(.vertical, 18)
    }
}
