//
//  AskAQuestionPage.swift
//  iosApp
//
//  Created by Said on 17/05/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import KMPNativeCoroutinesAsync

struct AskAQuestionPage: View {
    @State var question: String = ""
    @State var isAnonymous: Bool = false
    @State var loading: Bool = false
    @State var errorMessage: String? = nil
    @State var success: Bool = false
    @State var error: Bool = false
    
    var subjectId: String
    var title: String?
    var speakers: [Speaker]?
    
    @FocusState private var isFocused: Bool
    
    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            if let title = title {
                Text(title)
                    .font(.title)
                    .foregroundColor(Color("Primary"))
            }
            
            if let speakers = speakers {
                ForEach(speakers, id: \.self) { speaker in
                    Text("\(speakers.firstIndex(of: speaker) == 0 ? "": ", ")Dr. \(speaker.getFullName())")
                        .foregroundColor(Color("Secondary"))
                }
                .padding(.bottom, 20)
            }
            
            VStack(alignment: .trailing, spacing: 8) {
                
                VStack(alignment: .leading) {
                    TextView(text: $question)
                }
                .focused($isFocused)
                .padding(4)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(style: StrokeStyle(lineWidth: 1))
                        .foregroundColor(isFocused ? .accentColor : Color("Secondary"))
                        .overlay(
                            Text("Question")
                                .background(Color("Background"))
                                .font(.system(size: 14))
                                .foregroundColor(isFocused ? .accentColor : Color("Secondary"))
                                .offset(x: 10, y: -70)
                                .frame(maxWidth: .infinity, alignment: .leading)
                        )
                )
                
                HStack {
                    if let message = errorMessage {
                        Text(message)
                            .foregroundColor(Color.red)
                            .font(.system(size: 13))
                    }
                    
                    Spacer()
                    
                    Text("\(question.length) / 260")
                        .font(.system(size: 12))
                        .foregroundColor(Color("Secondary"))
                }
                .padding(.horizontal, 10)
            }
            
            HStack {
                CheckBoxView(checked: $isAnonymous)
                Text("Ask Anonymously")
                    .font(.system(size: 14))
                    .onTapGesture {
                        isAnonymous.toggle()
                    }
            }
            .padding(.bottom, 22)
            
            MultifunctionalButton(
                action: { Task { await askAQuestion() } },
                label: "Submit",
                isLoading: loading
            )
            
            Spacer()
        }
        .padding(.horizontal)
        .padding(.top, 15)
        .hideKeyboard()
        .alert("Success", isPresented: $success, actions: { }, message: {
            Text("Your question has been successfully posted.")
        })
        .alert("Failed", isPresented: $error, actions: { }, message: {
            Text("Something went wrong, please try again in a minute.")
        })
        .background(Color("Background"))
    }
    
    func askAQuestion() async {
        errorMessage = nil
        loading = true
        
        if question.isEmpty {
            errorMessage = "Question cannot be empty."
            loading = false
            return
        }
        
        let result = await asyncResult(for: AskQuestionUseCase().askQuestion(eventId: subjectId, question: question, isAnonymous: isAnonymous))
        
        switch result {
        case .success(let success):
            if success as? Bool == true {
                self.success = true
                question = ""
            } else {
                self.error = true
            }
            
        case .failure(_):
            self.error = true
        }
        
        loading = false
    }
}
