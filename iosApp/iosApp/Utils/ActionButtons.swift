//
//  ActionButtons.swift
//  iosApp
//
//  Created by marcjalkh on 02/10/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver
import shared

struct ActionButtons: View {
    @InjectedObject var deepLinkingViewModel: DeepLinkingViewModel
    let actions: [Action]
    
    @State var actionSuccessMessage: String = ""
    @State var actionFailedMessage: String = ""
    @State var actionSuccess: Bool = false
    @State var actionFailed: Bool = false
    
    @State var isLoading: Bool = false
    
    var body: some View {
        ForEach (actions, id: \.self) { action in
            Button {
                if action.type == Action.ActionType.openlink {
                    deepLinkingViewModel.checkDeepLink(action.url)
                } else {
                    Task {
                        defer {
                            isLoading = false
                        }
                        isLoading = true
                        actionFailedMessage = ""
                        
                        guard let res = try? await PostActionUseCase().handleIos(action: action) else {
                            actionFailed = true
                            actionFailedMessage = "Something Went Wrong!"
                            return
                        }
                        
                        res.fold(
                            onSuccess: { successMessage in
                                guard let message = successMessage as? String  else {
                                    return
                                }
                                actionSuccess = true
                                actionSuccessMessage = message
                            },
                            onFailure: { err in
                                actionFailed = true
                                actionFailedMessage = err.toUserFriendlyError()
                            }
                        )
                    }
                }
            } label: {
                Text(action.label)
                    .foregroundColor(Color("Default"))
                    .font(.system(size: 16))
                    .frame(maxWidth: .infinity)
                    .multilineTextAlignment(.center)
                    .padding(16)
                    .background(action.isPrimary ? Color("Primary") : Color("OnSurface-Secondary"))
                    .cornerRadius(16)
                    .overlay {
                        if isLoading {
                            CustomLoader(strokeColor: Color("Default"))
                                .background(
                                    RoundedRectangle(cornerRadius: 16)
                                        .foregroundColor(Color("Primary"))
                                )
                        }
                        
                    }
            }
            .disabled(action.isDisabled)
            .alert("Success", isPresented: $actionSuccess, actions: {}, message: {
                Text(actionSuccessMessage)
            })
            .alert("Failed", isPresented: $actionFailed, actions: {}, message: {
                Text(actionFailedMessage)
            })
        }
    }
}
