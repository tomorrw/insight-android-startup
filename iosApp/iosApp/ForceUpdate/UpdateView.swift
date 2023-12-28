//
//  UpdateView.swift
//  iosApp
//
//  Created by Said on 01/11/2022.
//  Copyright © 2022 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver
import shared

struct UpdateView: View {
    @StateObject var vm = UpdateViewModel()
    @Environment(\.openURL) private var openURL
    
    var body: some View {
        if vm.showingPopup {
            VStack(alignment: .center) {
                Spacer()
                
                VStack {
                    Text("Kindly update the \(vm.appName) to get the latest features.")
                        .font(.system(size: 18))
                        .multilineTextAlignment(.center)
                        .padding(.bottom)
                        .padding(.bottom)
                    
                    HStack {
                        Spacer()
                        
                        if (vm.isCancellable) {
                            Button(role: .cancel) {
                                vm.closePopup()
                            } label: {
                                Text("Cancel").font(.system(size: 18))
                            }
                            .foregroundColor(.red)
                            Spacer()
                        }
                        
                        Link("Update", destination: vm.url)
                            .font(.system(size: 18))
                            .foregroundColor(Color("HighlightPrimary"))
                        
                        Spacer()
                    }
                }
                .padding()
                .padding(.vertical)
                .background(Color("Background"))
                .frame(width: 300)
                .clipShape(RoundedRectangle(cornerRadius: 16))
                
                Spacer()
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(.black.opacity(0.6))
        }
    }
}

struct UpdateView_Previews: PreviewProvider {
    static var previews: some View {
        UpdateView()
    }
}
