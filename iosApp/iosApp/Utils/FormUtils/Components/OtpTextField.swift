//
//  OtpTextFielde.swift
//  iosApp
//
//  Created by Yammine on 4/19/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Combine

public struct OtpTextField: View {
    let maxLength: Int
    @Binding var otp: String
    @FocusState private var focusedField: FocusField?
    
    private enum FocusField: Hashable {
        case field
    }
    
    private var backgroundTextField: some View {
        return TextField("", text: $otp)
            .frame(width: 0, height: 0, alignment: .center)
            .font(Font.system(size: 0))
            .accentColor(.blue)
            .foregroundColor(.blue)
            .multilineTextAlignment(.center)
            .keyboardType(.numberPad)
            .focused($focusedField, equals: .field)
            .onReceive(Just(otp)) { _ in limitText(maxLength) }
            .task {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                    self.focusedField = .field
                }
            }
            .padding()
            .onAppear {
                UITextField.appearance().clearButtonMode = .never
                UITextField.appearance().tintColor = UIColor.clear
            }
    }
    
    public var body: some View {
        ZStack(alignment: .center) {
            backgroundTextField
            HStack {
                Spacer()
                ForEach(0..<maxLength) { index in
                    Text(otp[index] != "" ? otp[index] : "_")
                        .font(.system(size: 28))
                        .fontWeight(.semibold)
                        .frame(width: 40, height: 70)
                        .padding(.trailing, 5)
                        .padding(.leading, 5)
                        .overlay {
                            RoundedRectangle(cornerRadius: 16)
                                .stroke(otp.count == index ? Color("HighlightPrimary") : .gray, lineWidth: 2)
                        }
                        .overlay(
                            Button(action: {
                                self.focusedField = .field
                            }, label: {
                                Rectangle()
                                    .foregroundColor(.clear)
                            })
                            .containerShape(Rectangle())
                        )
                        
                    Spacer()
                }
            }
        }
    }
    
    func limitText(_ upper: Int) {
        if otp.count > upper {
            otp = String(otp.prefix(upper))
        }
    }
}

extension String {
    var length: Int {
        return count
    }
    
    subscript (i: Int) -> String {
        return self[i ..< i + 1]
    }
    
    func substring(fromIndex: Int) -> String {
        return self[min(fromIndex, length) ..< length]
    }
    
    func substring(toIndex: Int) -> String {
        return self[0 ..< max(0, toIndex)]
    }
    
    subscript (r: Range<Int>) -> String {
        let range = Range(uncheckedBounds: (lower: max(0, min(length, r.lowerBound)),
                                            upper: min(length, max(0, r.upperBound))))
        let start = index(startIndex, offsetBy: range.lowerBound)
        let end = index(start, offsetBy: range.upperBound - range.lowerBound)
        return String(self[start ..< end])
    }
}

struct OtpTextFielde_Previews: PreviewProvider {
    static var previews: some View {
        OtpTextField(maxLength: 1, otp: .constant(""))
    }
}
