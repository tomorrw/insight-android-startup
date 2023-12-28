//
//  ProfileSettingsPage.swift
//  iosApp
//
//  Created by marcjalkh on 02/10/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver

struct ProfileSettingsPage: View {
    @InjectedObject var authViewModel: AuthenticationViewModel
    @State var islogoutConfirmationDisplayed = false
    @State private var isDisplayingError = false
    
    var body: some View {
        VStack (spacing: 20){

            VStack (alignment: .leading) {
                
                Button(action: {
                    if let url = URL(string: "https://convenire.app/request-account-deletion") {
                        UIApplication.shared.open(url)
                    }
                }, label: {
                    Text("Delete Account & Archive Data")
                        .foregroundColor(Color("Error"))
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .contentShape(Rectangle())
                })
                .padding(.horizontal)
                .padding(.vertical, 5)
                Divider()
                Button(action: {
                    self.islogoutConfirmationDisplayed = true
                }, label: {
                    Text("Log Out")
                        .foregroundColor(Color("Error"))
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .contentShape(Rectangle())
                })
                .padding(.horizontal)
                .padding(.vertical, 5)
                .confirmationDialog(
                    "Log out",
                    isPresented: $islogoutConfirmationDisplayed
                ) {
                    Button("Log out", role: .destructive) { authViewModel.logout() }
                } message: {
                    Text("Are you sure you want to log out? all your changes will be lost.")
                }
                .alert("Logout Failed", isPresented: $isDisplayingError, actions: { }, message: {
                    Text(authViewModel.errorMessage ?? "Something Went Wrong!")
                })
                .onReceive(authViewModel.$errorMessage, perform: { error in
                    guard error != nil && error != "" else {
                        isDisplayingError = false
                        return
                    }
                    isDisplayingError = true
                })
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 10)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 10.0, style: .continuous))
            .padding(.vertical)

            Spacer()
        }
        .padding(.horizontal)
        .padding(.bottom)
        
        .frame(maxWidth: .infinity)
        .navigationTitle("Profile Settings")
        .navigationBarTitleDisplayMode(.inline)
        .background(Color("Background"))
    }

}


struct ProfileSettingsPage_Previews: PreviewProvider {
    static var previews: some View {
        ProfileSettingsPage()
    }
}
