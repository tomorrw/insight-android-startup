//
//  Switcher.swift
//  iosApp
//
//  Created by marcjalkh on 04/09/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct Switcher: View {
    @State var isSwitched:Bool = false
    var callbackFun: () -> Void
    var body: some View {
        Button {
            callbackFun()
            isSwitched.toggle()
        } label: {
            ZStack {
                RoundedRectangle(cornerRadius: 8)
                    .fill(.white)
                    .frame(width: 60)
                HStack ( spacing: 0){
                    Image(systemName: isSwitched ? "location.fill" : "person.fill")
                    Image(systemName: "chevron.up.chevron.down")
                        .padding(.leading, 5)
                }
                .frame(width: 60)
            }
            .padding(.top,10)
        }
    }
}

