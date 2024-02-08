//
//  Checkbox.swift
//  iosApp
//
//  Created by Said on 17/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct CheckBoxView: View {
    @Binding var checked: Bool
    var size: CGFloat = 16

    var body: some View {
        Button {
            self.checked.toggle()
        } label: {
            Image(systemName: checked ? "checkmark.square.fill" : "square")
                .font(.system(size: size))
                .foregroundColor(Color("Primary"))
                .contentShape(Rectangle())
        }
        .frame(width: 40, height: 40)
        .contentShape(Rectangle())
    }
}

struct CheckBoxView_Previews: PreviewProvider {
    struct CheckBoxViewHolder: View {
        @State var checked = false

        var body: some View {
            CheckBoxView(checked: $checked)
        }
    }

    static var previews: some View {
        CheckBoxViewHolder()
    }
}

