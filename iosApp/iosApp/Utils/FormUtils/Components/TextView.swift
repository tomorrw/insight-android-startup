//
//  TextView.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct UITextViewWrapper: UIViewRepresentable {
    @Binding var text: String
    
    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    func makeUIView(context: Context) -> UITextView {
        let textField = UITextView()
        textField.font = UIFont(name: "ArialMT", size: 16)
//        textField.font = UIFont(name: "ReadexPro-Regular", size: 16)
        textField.backgroundColor = UIColor.clear
//        return context.coordinator.textView
        textField.delegate = context.coordinator
        return textField
    }
    
    func updateUIView(_ uiView: UITextView, context: Context) {
        // update in case the text value has changed, we assume the UIView checks if the value is different before doing any actual work.
        // fortunately UITextView doesn't call its delegate when setting this property (in case of MKMapView, we would need to set our did change closures to nil to prevent infinite loop).
        uiView.text = text

        // since the binding passed in may have changed we need to give a new closure to the coordinator.
        context.coordinator.textDidChange = { newText in
            text = newText
        }
    }
    
    class Coordinator: NSObject, UITextViewDelegate {
        lazy var textView: UITextView = {
            let textView = UITextView()
            textView.font = .preferredFont(forTextStyle: .body)
            textView.delegate = self
            return textView
        }()
        
        var textDidChange: ((String) -> Void)?
        
        func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
            textView.text.count + (text.count - range.length) <= 260
        }
        
        func textViewDidChange(_ textView: UITextView) {
            textDidChange?(textView.text)
        }
    }
}

struct TextView: View {

    private var placeholder: String
    private var onCommit: (() -> Void)?

    @Binding private var text: String
    private var internalText: Binding<String> {
        Binding<String>(get: { self.text } ) {
            self.text = $0
            self.showingPlaceholder = $0.isEmpty
        }
    }

    @State private var dynamicHeight: CGFloat = 130
    @State private var showingPlaceholder = false

    init (_ placeholder: String = "", text: Binding<String>, onCommit: (() -> Void)? = nil) {
        self.placeholder = placeholder
        self.onCommit = onCommit
        self._text = text
        self._showingPlaceholder = State<Bool>(initialValue: self.text.isEmpty)
    }

    var body: some View {
        UITextViewWrapper(text: self.internalText)
            .frame(minHeight: dynamicHeight, maxHeight: dynamicHeight)
            .frame(maxWidth: .infinity)
            .clipped()
            .background(placeholderView, alignment: .topLeading)
    }

    var placeholderView: some View {
        Group {
            if showingPlaceholder {
                Text(placeholder).foregroundColor(.gray)
                    .padding(.leading, 4)
                    .padding(.top, 8)
            }
        }
    }
}
