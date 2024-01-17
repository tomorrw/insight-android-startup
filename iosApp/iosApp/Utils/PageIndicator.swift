//
//  PageIndicator.swift
//  iosApp
//
//  Created by Yammine on 4/19/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

// can only be used with SwipePages
struct PageIndicator: View {
    var pagesCount: Int
    var backgroundOffset: CGFloat // same value passed to swipePages
    
    var body: some View {
        HStack {
            ForEach(0..<pagesCount, id: \.self) { index in
                Circle()
                    .frame(width: 8, height: 8)
                    .foregroundColor(CGFloat(index) == backgroundOffset ? Color("Secondary-default") : Color ("Black-outline"))
            }
        }
    }
}

struct PageIndicator_Previews: PreviewProvider {
    static var previews: some View {
        PageIndicator(pagesCount: 2, backgroundOffset: 1)
    }
}
