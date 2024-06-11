//
//  AuthenticationViewModel.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import Combine
import KMPNativeCoroutinesAsync
import Firebase
import UiComponents

class AuthenticationViewModel: ObservableObject {
    @Published var isAuthenticated: Bool? = nil
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = ""
    @Published var awaitingVerification: Bool = false
    @Published var canSendOtp = true
    @Published var timeBeforeSendingNewOtp: Int = 60
    @Published var user: User? = nil
    @Published var agreed: Bool = false
    
    private var otpUuid: String? = nil
    private var cancellable: AnyCancellable? = nil
    private let otpCooldown = 60
    private let registerUseCase = RegisterUseCase()
    private let verifyUseCase = VerifyOTPUseCase()
    private let logoutUseCase = LogoutUseCase()
    private let loginUseCase = LoginUseCase()
    
    @MainActor var getUserTask: Task<(), Never>? = nil
    @MainActor var getIsAuthenticatedTask: Task<(), Never>? = nil
    
    init() {
        DispatchQueue.main.async {
            self.getIsAuthenticated()
        }
    }
    
    @MainActor func getUser(firstTime: Bool = false) {
        getUserTask?.cancel()
        getUserTask = Task {
            do {
                let result = asyncSequence(for: GetUserUseCase().getUser())
                
                for try await userResult in result {
                    self.user = userResult
                    try await Messaging.messaging().subscribe(toTopic: "user_\(userResult.id)")
                    try await Messaging.messaging().subscribe(toTopic: "user_registered")
                }
                
            } catch {
                print(error)
            }
        }
    }
    
    @MainActor func getIsAuthenticated() {
        getIsAuthenticatedTask?.cancel()
        getIsAuthenticatedTask = Task {
            do {
                let result = asyncSequence(for: IsAuthenticatedUseCase().asFlow())
                
                for try await isAuthenticatedResult in result {
                    self.isAuthenticated = isAuthenticatedResult as? Bool
                    self.getUser() // TODO: scary stuff, take another look
                }
            } catch {
                print(error)
            }
        }
    }
    
    @MainActor func login(_ form: FormData, isLogingViaEmail: Bool) {
        Task {
            defer {
                isLoading = false
            }
            isLoading = true
            errorMessage = nil
            
            var phoneNumber :String? = form.fieldValue(for: "phoneNumber")
            var email : String?
            if isLogingViaEmail {
                email = form.fieldValue(for: "email")
                phoneNumber = nil
            }
            
            guard let res = try? await loginUseCase.loginIOS(phoneNumber: phoneNumber, email: email) else {
                errorMessage = "Something Went Wrong!"
                return
            }
            
            res.fold(
                onSuccess: { [weak self] uuidAny in
                    guard let self = self else { return }
                    guard let uuid = uuidAny as? String  else {
                        return
                    }
                    self.awaitingVerification = true
                    self.otpUuid = uuid
                },
                onFailure: { [weak self] err in
                    guard let self = self else { return }
                    self.otpUuid = nil
                    self.errorMessage = err.toUserFriendlyError()
                }
            )

        }
    }
    
    @MainActor func register(_ form: FormData) {
        errorMessage = nil
        if !agreed {
            self.errorMessage = "You must agree to the Terms & Conditions!"
        } else {
            Task {
                defer {
                    isLoading = false
                }
                isLoading = true
                
                try? await registerUseCase.registerIOS(
                    firstName: form.fieldValue(for: "firstName"),
                    lastName: form.fieldValue(for: "lastName"),
                    email: form.fieldValue(for: "email"),
                    phoneNumber: form.fieldValue(for: "phoneNumber")
                ).fold(
                    onSuccess: { [weak self] uuidAny in
                        guard let self = self else { return }
                        guard let uuid = uuidAny as? String  else {
                            return
                        }
                        self.awaitingVerification = true
                        self.otpUuid = uuid
                    },
                    onFailure: { [weak self] err in
                        guard let self = self else { return }
                        self.otpUuid = nil
                        self.errorMessage = err.toUserFriendlyError()
                    }
                )
            }
        }
    }
    
    @MainActor func verify(otp: String, phoneNumber: String?, email: String?) {
        Task {
            defer {
                isLoading = false
            }
            isLoading = true
            errorMessage = nil
            
            guard let res = try? await verifyUseCase.verifyIOS(phoneNumber: phoneNumber, email: email, otp: otp) else {
                errorMessage = "Something Went Wrong!"
                return
            }
            
            res.fold { [weak self] _ in
                guard let self = self else { return }
                self.isAuthenticated = true
            } onFailure: { [weak self] err in
                guard let self = self else { return }
                self.errorMessage = err.toUserFriendlyError()
            }
        }
    }
    
    @MainActor func logout() {
        Task {
            defer {
                isLoading = false
            }
            isLoading = true
            errorMessage = nil
            
            guard let res = try? await logoutUseCase.logoutIOS() else {
                errorMessage = "Something Went Wrong!"
                return
            }
            
            res.fold { [weak self] _ in
                guard let self = self else { return }
                self.otpUuid = nil
                self.cancellable = nil
                self.isAuthenticated = false
                self.awaitingVerification = false
                self.canSendOtp = true
                Task {
                    do {
                        try await Messaging.messaging().deleteToken()
                    } catch {
                        print(error)
                    }
                }
            } onFailure: { [weak self] err in
                guard let self = self else { return }
                self.errorMessage = err.toUserFriendlyError()
            }
        }
    }
    
    private func launchOtpTimer() {
        canSendOtp = false
        timeBeforeSendingNewOtp = otpCooldown
        
        cancellable = Timer
            .publish(every: 1, on: .main, in: .common)
            .autoconnect()
            .sink { [weak self] _ in
                guard let self = self else { return }
                
                guard self.timeBeforeSendingNewOtp > 0 else {
                    self.cancellable = nil
                    self.timeBeforeSendingNewOtp = self.otpCooldown
                    self.canSendOtp = true
                    return
                }
                
                self.timeBeforeSendingNewOtp -= 1
            }
    }
    
    private func stopTimer() {
        canSendOtp = true
        timeBeforeSendingNewOtp = otpCooldown
        cancellable = nil
    }
    
    func toggleAgreed() {
        agreed.toggle()
    }
}
