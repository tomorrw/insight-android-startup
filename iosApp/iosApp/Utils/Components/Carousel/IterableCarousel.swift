//
//  IterableCarousel.swift
//  iosApp
//
//  Created by Said on 16/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct IterableCarousel<Data: RandomAccessCollection, Content: View> : View where Data.Element: Hashable {
    @State var backgroundOffset: CGFloat = 0
    @State var temporaryOffset: CGFloat = 0
    
    @Binding var itemWidth: CGFloat
    var timed: Bool
    
    var data: Data
    @ViewBuilder var content: (Data.Element) -> Content
    
    init(_ data: Data, itemWidth: Binding<CGFloat>, timed: Bool = true, @ViewBuilder content: @escaping (Data.Element) -> Content) {
        self.data = data
        _itemWidth = itemWidth
        self.content = content
        self.timed = timed
    }
    
    var dimensions = CarouselItemDimensions.shared.normalDimensions

    var body: some View {
        VStack(spacing: 12) {
            GeometryReader { geo in
                
                HStack(spacing: 0) {
                    ForEach(data, id: \.self) { item in
                        content(item)
                    }
                }
                .frame(alignment: .leading)
                .swipePages(backgroundOffset: $backgroundOffset, offsetWidth: itemWidth, pagesCount: data.count, timed: timed)
                .frame(maxWidth: .infinity)
                .onAppear {
                    itemWidth = geo.size.width
                }

            }
            .frame(maxWidth: .infinity)
            .frame(height: UIScreen.main.bounds.width * 0.3)

            PageIndicator(pagesCount: data.count, backgroundOffset: backgroundOffset)
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical)
    }
}
