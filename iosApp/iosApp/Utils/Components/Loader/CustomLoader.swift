//
//  CustomLoader.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct CustomLoader: View {
    @State private var isLoading: Bool = false
    var strokeColor: Color = Color("Primary")
    
    var body: some View {
        VStack {
            Circle()
                .trim(from: !isLoading ? 0 : 1, to: !isLoading ? 0 : 2)
                .stroke(strokeColor, style: StrokeStyle(lineWidth: 5, lineCap: .round))
                .frame(width: 30, height: 30)
                .animation(Animation.linear(duration: 0.8).repeatForever(autoreverses: false), value: self.isLoading)
                .onAppear() {
                    DispatchQueue.main.async {
                        self.isLoading = true
                    }
                }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

struct CustomLoader_Previews: PreviewProvider {
    static var previews: some View {
        CustomLoader()
    }
}
