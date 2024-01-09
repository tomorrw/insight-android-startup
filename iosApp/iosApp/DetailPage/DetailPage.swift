//
//  CompanyPage.swift
//  iosApp
//
//  Created by Yammine on 4/25/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import KMPNativeCoroutinesAsync
import Resolver

struct DetailPage<Header: View, Body: View>: View {
    @State private var isDisplayingError = false
    @ObservedObject var vm: DetailPageViewModel
    
    var customHeader: () -> Header
    var customBody: () -> Body
    
    init(
        isDisplayingError: Bool = false,
        vm: DetailPageViewModel,
        @ViewBuilder customHeader: @escaping () -> Header = { EmptyView() },
        @ViewBuilder customBody: @escaping () -> Body = { EmptyView() }
    ) {
        self.isDisplayingError = isDisplayingError
        self.vm = vm
        self.customHeader = customHeader
        self.customBody = customBody
    }
    
    var body: some View {
        Group {
            if !vm.isLoading {
                ScrollView {
                    VStack(alignment: vm.headerDesign == .contact ? .center : .leading, spacing: 12) {
                        if (vm.headerDesign == .contact) {
                            ZStack(alignment: .bottomTrailing) {
                                UrlImageView(urlString: vm.image)
                                    .scaledToFill()
                                    .clipShape(Circle())
                                    .frame(width: 72, height: 72)
                                
                                if let icon = vm.imagePinIcon {
                                    UrlImageView(urlString: icon)
                                        .clipShape(Circle())
                                        .frame(width: 23, height: 23)
                                }
                            }
                            .frame(alignment: .bottomTrailing)
                            
                        } else {
                            UrlImageView(urlString: vm.image)
                                .frame(maxWidth: .infinity)
                                .aspectRatio(contentMode: .fit)
                                .clipShape(RoundedRectangle(cornerRadius: 16))
                                .padding(.bottom, 4)
                        }
                        
                        VStack(alignment: vm.headerDesign == .contact ? .center : .leading) {
                            Group {
                                Text(vm.title)
                                    .font(.system(size: 20, weight: .medium))
                                    .padding(.bottom, 1)
                                
                                Text(vm.description)
                                    .foregroundColor(Color("Secondary"))
                                    .padding(.bottom, 16)
                            }
                            .multilineTextAlignment(vm.headerDesign == .contact ? .center : .leading)
                        }
                        
                        customHeader()
                    }
                    .padding()
                    
                    customBody()
                }
            } else {
                // loaders
                switch vm.headerDesign {
                case .contact: VStack {
                    Circle()
                        .fill(.gray.opacity(0.3))
                        .frame(width: 72, height: 72)
                        .padding(.bottom)
                    RoundedRectangle(cornerRadius: 16)
                        .fill(.gray.opacity(0.3))
                        .frame(width: 100, height: 22)
                    RoundedRectangle(cornerRadius: 16)
                        .fill(.gray.opacity(0.3))
                        .frame(width: 150, height: 22)
                    Spacer()
                }.frame(maxWidth: .infinity)
                case .detailPage: VStack(alignment: .leading) {
                    RoundedRectangle(cornerRadius: 16)
                        .fill(.gray.opacity(0.3))
                        .frame(maxWidth: .infinity)
                        .aspectRatio(16/9, contentMode: .fit)
                    
                    RoundedRectangle(cornerRadius: 16)
                        .fill(.gray.opacity(0.3))
                        .frame(width: 150, height: 22)
                    Spacer()
                }.padding()
                }
            }
        }
        .alert("Load Failed.", isPresented: $isDisplayingError, actions: { }, message: {
            Text(vm.errorMessage ?? "Something Went Wrong!")
        })
        .onReceive(vm.$errorMessage, perform: { error in
            guard error != nil && error != "" else {
                isDisplayingError = false
                return
            }
            isDisplayingError = true
        })
        
    }
}

