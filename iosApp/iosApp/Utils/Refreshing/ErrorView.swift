//
//  RetryView.swift
//  iosApp
//
//  Created by Yammine on 11/7/22.
//  Copyright © 2022 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared

struct ErrorView: View {
    let action: () async -> Void
    var errorText: ErrorText = .retry
    
    var body: some View {
        VStack {
            Text("Something Went Wrong!")
                .font(.custom("ReadexPro-Medium", size: 25))
            Button {
                Task {
                    await action()
                }
            } label: {
                Text(errorText == .retry ? "Try Again" : "Go Back")
                    .font(.custom("ReadexPro-Regular", size: 18))
                    .padding()
                    .foregroundColor(Color.white)
                    .background(
                        RoundedRectangle(cornerRadius: 16)
                            .background(
                                Color("Black-lowEmphasis")
                            )
                            .clipShape(
                                RoundedRectangle(cornerRadius: 16)
                            )
                        )
            }

        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

enum ErrorText {
    case goBack, retry
}

//struct RetryView_Previews: PreviewProvider {
//    static var previews: some View {
//        RetryView()
//    }
//}
