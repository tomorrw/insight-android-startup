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
    
    @AppStorage("selectedColorTheme") private var selectedColorTheme = "Auto"

    var body: some View {
        Form{
            
            Section {
                Picker(selection: $selectedColorTheme, label: Text("Color Theme")) {
                    ForEach(["Light", "Auto", "Dark"], id: \.self) {
                        Text($0).tag($0)
                    }
                }
                .pickerStyle(SegmentedPickerStyle())
            } header: {
                Text("Color Theme")
                    .font(.custom("Outfit", size: 14))
            } footer: {
                Text("The Auto option means that the color theme selected will be the current one on your device.")
                    .font(.custom("Outfit", size: 14))
            }
            .listRowBackground(Color.clear)
            
            Section {
                Button {
                    if let url = URL(string: "https://api.convenire.app/request-account-deletion") {
                        UIApplication.shared.open(url)
                    }
                } label: {
                    Label("Delete Account & Archive Data", systemImage: "trash")
                        .font(.custom("Outfit", size: 16))
                        .foregroundColor(Color("Error"))
                }
                
                Button {
                    self.islogoutConfirmationDisplayed = true
                } label: {
                    Label("Log Out", systemImage: "rectangle.portrait.and.arrow.right")
                        .font(.custom("Outfit", size: 16))
                        .foregroundColor(Color("Error"))
                }
            }
            .listRowBackground(Color("Default"))
            
        }
        .frame(maxWidth: .infinity)
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
        .background(Color("Background"))
    }
    
}


struct ProfileSettingsPage_Previews: PreviewProvider {
    static var previews: some View {
        ProfileSettingsPage()
    }
}
