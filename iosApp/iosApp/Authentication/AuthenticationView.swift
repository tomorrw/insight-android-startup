//
//  AuthScreen.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Resolver
import shared
import UiComponents
import ios_project_startup

fileprivate func getCountryFlag(_ isoCode: String) -> String {
    let base : UInt32 = 127397
    var flag = ""
    for x in isoCode.uppercased().unicodeScalars {
        flag.unicodeScalars.append(UnicodeScalar(base + x.value)!)
    }
    return flag
}

struct AuthenticationView: View {
    @InjectedObject var authViewModel: AuthenticationViewModel
    @FocusState private var focusedField: FocusedField?
    @StateObject var signUpFormData = FormData(fields: [
        "firstName": Field("First Name"),
        "lastName": Field("Last Name"),
        "phoneNumber": Field("Phone Number"),
        "email": Field("Email Address (optional)")
    ])
    @StateObject var signInFormData = FormData(fields: [
        "phoneNumber": Field("Phone Number"),
        "email": Field("Email Address")
    ])
    
    @State private var otp = ""
    @State private var isDisplayingError = false
    @State var phone = SharedPhoneNumber(number: nil)
    @State var email : String = ""
    @State private var isLogin: Bool = true
    @State var isLoginViaEmail: Bool = false
    
    enum FocusedField {
        case firstName
        case lastName
        case email
        case phoneNumber
    }
    
    var body: some View {
        ZStack {
            Color.black.opacity(0.01)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .onTapGesture {
                    focusedField = nil
                }
            
            if !authViewModel.awaitingVerification {
                
                if isLogin {
                    signInForm
                } else {
                    signUpForm
                }
                
            } else {
                VStack(spacing: 28) {
                    HStack {
                        Button { authViewModel.awaitingVerification = false } label: {
                            Label("Back", systemImage: "chevron.left")
                        }
                        
                        Spacer()
                    }
                    .padding(.top, 16)
                    
                    Spacer()
                    
                    if authViewModel.isLoading {
                        CustomLoader(strokeColor: Color("HighlightPrimary"))
                            .frame(height: 50)
                    } else {
                        VStack {
                            Text("Enter OTP")
                                .font(.system(size: 28, weight: .semibold))
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.bottom, 4)
                            
                            Text("An \(self.isLoginViaEmail ? "Email" : "SMS" ) will arrive to \(self.isLoginViaEmail ? email : (phone.number ?? "")) shortly, containing your OTP")
                                .font(.system(size: 16))
                                .foregroundColor(Color("Secondary"))
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        .padding(.bottom, 8)
                        
                        OtpTextField(maxLength: 4, otp: $otp, highlightedColor: Color("HighlightPrimary"))
                            .onChange(of: otp) { newOtp in
                                if newOtp.count == 4 {
                                    authViewModel.verify(otp: newOtp, phoneNumber: isLoginViaEmail ? nil : phone.number, email: isLoginViaEmail ? email : nil)
                                    otp = ""
                                }
                            }
                        
                        VStack(spacing: 0) {
                            if authViewModel.canSendOtp {
                                HStack {
                                    Spacer()
                                    
                                    Group {
                                        Text("Didn't receive code? ")
                                        Button { authViewModel.login(signInFormData, isLogingViaEmail: isLoginViaEmail) } label: {
                                            Text("Resend OTP")
                                                .foregroundColor(Color("HighlightPrimary"))
                                        }
                                    }
                                    .font(.system(size: 16))
                                    
                                    Spacer()
                                }
                                
                            } else {
                                Text("Resend after \(authViewModel.timeBeforeSendingNewOtp) sec.")
                                    .font(.system(size: 16))
                            }
                            
                            HStack {
                                Spacer()
                                
                                Group {
                                    Text("Not your \(self.isLoginViaEmail ? "email" : "number" )? ")
                                    Button { authViewModel.awaitingVerification = false } label: {
                                        Text("Change \(self.isLoginViaEmail ? "email" : "number" )")
                                            .foregroundColor(Color("HighlightPrimary"))
                                            .padding(.vertical)
                                    }
                                }
                                .font(.system(size: 16))
                                
                                Spacer()
                            }
                        }
                        .padding(.top, 12)
                    }
                    
                    Spacer()
                }
                .alert("Verification Failed", isPresented: $isDisplayingError, actions: { }, message: {
                    Text(authViewModel.errorMessage ?? "Something Went Wrong!")
                })
                .padding()
            }
        }
        .onReceive(authViewModel.$errorMessage, perform: { error in
            guard error != nil && error != "" else {
                isDisplayingError = false
                return
            }
            isDisplayingError = true
        })
        .hideKeyboard()
        .background(Color("Background"))
    }
    
    var signUpForm: some View {
        
        
        ZStack {

            ScrollView {
                
                VStack(spacing: 22) {
                    title
                    
                    CustomTextField(
                        textValue: signUpFormData.bindingFieldValue(for: "firstName"),
                        icon: Image(systemName: "person"),
                        placeholderText: signUpFormData.fieldLabel(for: "firstName"),
                        showPlaceholder: signUpFormData.isFieldEmpty("firstName"),
                        hasError: signUpFormData.hasErrors,
                        error: signUpFormData.fieldError(for: "firstName"),
                        color: CustomTextFieldColors()
                    )
                    .focused($focusedField, equals: .firstName)
                    .textContentType(.givenName)
                    .submitLabel(.next)
                    CustomTextField(
                        textValue: signUpFormData.bindingFieldValue(for: "lastName"),
                        icon: Image(systemName: "person"),
                        placeholderText: signUpFormData.fieldLabel(for: "lastName"),
                        showPlaceholder: signUpFormData.isFieldEmpty("lastName"),
                        hasError: signUpFormData.hasErrors,
                        error: signUpFormData.fieldError(for: "lastName")
                    )
                    .focused($focusedField, equals: .lastName)
                    .textContentType(.familyName)
                    .submitLabel(.next)
                    
                    CustomTextField(
                        textValue: signUpFormData.bindingFieldValue(for: "phoneNumber", set: {
                            phone.number = $0
                            return phone.number ?? $0
                        }),
                        icon: Image(systemName: "phone"),
                        placeholderText: signUpFormData.fieldLabel(for: "phoneNumber"),
                        showPlaceholder: signUpFormData.isFieldEmpty("phoneNumber"),
                        hasError: signUpFormData.hasErrors,
                        error: signUpFormData.fieldError(for: "phoneNumber")
                    )
                    .focused($focusedField, equals: .phoneNumber)
                    .textContentType(.telephoneNumber)
                    .submitLabel(.next)
                    .keyboardType(.phonePad)
                    
                    CustomTextField(
                        textValue: signUpFormData.bindingFieldValue(for: "email"),
                        icon: Image(systemName: "envelope"),
                        placeholderText: signUpFormData.fieldLabel(for: "email"),
                        showPlaceholder: signUpFormData.isFieldEmpty("email"),
                        hasError: signUpFormData.hasErrors,
                        error: signUpFormData.fieldError(for: "email")
                    )
                    .focused($focusedField, equals: .email)
                    .textContentType(.emailAddress)
                    .submitLabel(.done)
                    .keyboardType(.emailAddress)
                    .textInputAutocapitalization(.never)
                    .padding(.bottom, 8)
                    
                    agreeCheckbox
                    
                    MultifunctionalButton(
                        action: { authViewModel.register(signUpFormData) },
                        label: "Register",
                        colors: DefaultColors.buttonColor,
                        isLoading: authViewModel.isLoading
                    )
                    .alert("Registration Failed", isPresented: $isDisplayingError, actions: { }, message: {
                        Text(authViewModel.errorMessage ?? "Something Went Wrong!")
                    })
                    .task {
                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                            self.focusedField = .firstName
                        }
                    }
                    
                    
                    
                    HStack {
                        
                        Text("Already have an account?")
                        
                        Button {
                            isLogin.toggle()
                        } label: {
                            Text("Login")
                                .foregroundColor(Color("HighlightPrimary"))
                        }
                        
                    }
                }
                .padding()
                .frame(height: UIScreen.main.bounds.height * 0.9)
                .onSubmit {
                    switch focusedField {
                    case .firstName:
                        focusedField = .lastName
                    case .lastName:
                        focusedField = .phoneNumber
                    case .phoneNumber:
                        focusedField = .email
                    default:
                        authViewModel.register(signUpFormData)
                    }
                }
                .onTapGesture {
                    focusedField = .none
                }
                
            }
            .padding(.top, 1)
        }
    }
    
    var signInForm: some View {
        VStack(spacing: 22) {
            title
            if(isLoginViaEmail){
                CustomTextField(
                    textValue: signInFormData.bindingFieldValue(for: "email", set: {
                        email = $0
                        return email
                    }),
                    icon: Image(systemName: "envelope"),
                    placeholderText: signInFormData.fieldLabel(for: "email"),
                    showPlaceholder: signInFormData.isFieldEmpty("email"),
                    hasError: signInFormData.hasErrors,
                    error: signInFormData.fieldError(for: "email")
                )
                .focused($focusedField, equals: .email)
                .textContentType(.emailAddress)
                .submitLabel(.next)
                .keyboardType(.emailAddress)
                .textInputAutocapitalization(.never)
                .padding(.bottom, 8)
            }
            else{
                CustomTextField(
                    textValue: signInFormData.bindingFieldValue(for: "phoneNumber", set: {
                        phone.number = $0
                        return phone.number ?? $0
                    }),
                    icon: Image(systemName: "phone"),
                    placeholderText: signInFormData.fieldLabel(for: "phoneNumber"),
                    showPlaceholder: signInFormData.isFieldEmpty("phoneNumber"),
                    hasError: signInFormData.hasErrors,
                    error: signInFormData.fieldError(for: "phoneNumber")
                )
                .focused($focusedField, equals: .phoneNumber)
                .textContentType(.telephoneNumber)
                .submitLabel(.next)
                .keyboardType(.phonePad)
                .padding(.bottom, 8)
            }
            MultifunctionalButton(
                action: { authViewModel.login(signInFormData , isLogingViaEmail: isLoginViaEmail) },
                label: "Login",
                colors: DefaultColors.buttonColor,
                isLoading: authViewModel.isLoading
            )
            .alert("Registration Failed", isPresented: $isDisplayingError, actions: { }, message: {
                Text(authViewModel.errorMessage ?? "Something Went Wrong!")
            })
            .task {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                    self.focusedField = .phoneNumber
                }
            }
            
            HStack {
                
                Text("Don't have an account?")
                
                Button {
                    isLogin.toggle()
                } label: {
                    Text("Sign up")
                        .foregroundColor(Color("HighlightPrimary"))
                }
                
            }
            HStack {
                
                Button {
                    isLoginViaEmail.toggle()
                } label: {
                    Text("Click here")
                        .foregroundColor(Color("HighlightPrimary"))
                }
                
                
                
                Text("to login with your \(isLoginViaEmail ? "phone number" : "email")")
                
            }
        }
        .padding()
        .onSubmit {
            authViewModel.register(signUpFormData)
        }
    }
    
    var title: some View {
        VStack {
            Text(isLogin ? "Login" : "Register")
                .font(.system(size: 28, weight: .semibold))
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.bottom, 4)
            
            Text("An \(isLoginViaEmail ? "email" : "sms" ) will arrive shortly containing your OTP")
                .font(.system(size: 16))
                .foregroundColor(Color("Secondary"))
                .frame(maxWidth: .infinity, alignment: .leading)
        }
        .padding(.bottom, 8)
    }
    
    var agreeCheckbox: some View {
        HStack(spacing: 8) {
            
            Button {
                authViewModel.toggleAgreed()
            } label: {
                HStack {
                    Image(systemName: authViewModel.agreed ? "checkmark.square.fill" : "square")
                        .font(.system(size: 18))
                        .foregroundColor(Color("Primary"))
                }
                .frame(width: 40, height: 40)
                .contentShape(Rectangle())
            }
            
            VStack(alignment: .leading) {
                Text("I have read and agree to the ")
                    .onTapGesture {
                        authViewModel.toggleAgreed()
                    }
                Link("Terms & Conditions", destination: URL(string: "https://api.convenire.app/privacy-policy")!)
                    .foregroundColor(Color("HighlightPrimary"))
            }
            .frame(maxWidth: .infinity, alignment: .leading)        }
    }
}

struct AuthenticationView_Previews: PreviewProvider {
    static var previews: some View {
        AuthenticationView()
    }
}
