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

struct DetailPage: View {
    @State private var isDisplayingError = false
    @ObservedObject var vm: DetailPageViewModel
    
    var customHeader: (() -> any View)?

    init(isDisplayingError: Bool = false, vm: DetailPageViewModel, @ViewBuilder customHeader: @escaping () -> any View) {
        self.isDisplayingError = isDisplayingError
        self.vm = vm
        self.customHeader = customHeader
    }
    
    init(isDisplayingError: Bool = false, vm: DetailPageViewModel) {
        self.isDisplayingError = isDisplayingError
        self.vm = vm
        self.customHeader = nil
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
                        
                        if vm.hasAttended {
                            Text("ATTENDED")
                                .font(.system(size: 14))
                                .padding(5)
                                .background(
                                    RoundedRectangle(cornerRadius: 8)
                                        .foregroundColor(.accentColor)
                                )
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
                        
                        if customHeader != nil {
                            AnyView(self.customHeader!())
                        }
                        
                        Divider().padding(.bottom, 5)
                        
                        LazyVStack(alignment: .leading, spacing: 32) {
                            ForEach(vm.sections) { section in
                                VStack(alignment: .leading) {
                                    let info = section.getInfo()
                                    
                                    VStack {
                                        if let image = (info as? InfoContent)?.imageUrl {
                                            UrlImageView(urlString: image)
                                                .frame(maxWidth: .infinity)
                                                .aspectRatio(contentMode: .fit)
                                                .clipShape(RoundedRectangle(cornerRadius: 16))
                                                .padding(.bottom, 4)
                                        }
                                    }
                                    
                                    Text(info.title)
                                        .font(.system(size: 18, weight: .medium))
                                        .padding(.bottom, 2)
                                    Text(info.description)
                                        .foregroundColor(Color("Secondary"))
                                        .font(.system(size: 15))
                                        .lineSpacing(3)
                                    
                                    switch(section) {
                                    case let .video(details): if let url = URL(string: details.videoUrl) {
                                        CustomVideoPlayer(url: url)
                                            .aspectRatio(16/9, contentMode: .fit)
                                            .background(.gray.opacity(0.3))
                                            .frame(maxWidth: .infinity)
                                            .clipShape(RoundedRectangle(cornerRadius: 16))
                                            .padding(.vertical, 2)
                                    } else { EmptyView() }
                                    case let .sessions(details): SessionsVerticalView(sessions: details.sessions)
                                    case let .speakers(details): SpeakersHorizontalView(speakers: details.speakers)
                                    default : EmptyView()
                                    }
                                }
                            }
                        }
                        
                        ActionButtons(actions: vm.action)
                        
                        Spacer()
                    }
                    .padding()
                    .padding(.bottom, 30)
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

