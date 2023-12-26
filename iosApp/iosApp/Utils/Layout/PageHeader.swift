//
//  PageHeader.swift
//  iosApp
//
//  Created by Yammine on 4/20/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct PageHeader<Content: View>: View {
    @ViewBuilder let content: Content
    
    var body: some View {
        VStack {
            content
        }
        .frame(maxWidth: .infinity)
        .navigationBarBackButtonHidden(true)
    }
}

struct PageHeader_Previews: PreviewProvider {
    static var previews: some View {
        PageHeader {
            
        }
    }
}
