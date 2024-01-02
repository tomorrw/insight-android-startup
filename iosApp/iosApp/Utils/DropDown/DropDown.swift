//
//  DropDown.swift
//  iosApp
//
//  Created by marcjalkh on 25/08/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI

struct DropDown: View {
    @State var choices: Array<String>
    @State var choiceMade: String

    var callbackFunct: (String) -> Void

    var body: some View {
        
        VStack{
            Menu{
                ForEach(choices.indices, id: \.self){i in
                    Button(action: {
                        callbackFunct(choices[i])
                    }, label: {
                        Text(choices[i])
                    })
                }
            } label: {
                HStack{
                    Text(choiceMade)
                    Spacer()
                    Image(systemName: "chevron.down")
                }
                .padding([.leading, .trailing], 15)
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .padding([.top,.bottom], 18)
            .frame(maxWidth: .infinity)
            .background(Color("Default"))
            .clipShape(RoundedRectangle(cornerRadius: 8))
            
        }
        .padding(.top, 10)
        
    }
}

//struct DropDown_Previews: PreviewProvider {
//    static var previews: some View {
//        DropDown()
//    }
//}
