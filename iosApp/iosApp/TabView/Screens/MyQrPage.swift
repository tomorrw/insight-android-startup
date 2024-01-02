//
//  MyQrPage.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import UIKit
import SwiftUI
import shared
import Resolver
import CoreImage.CIFilterBuiltins

struct MyQrPage: View {
    @InjectedObject var authViewModel: AuthenticationViewModel
    @InjectedObject var ticketViewModel: TicketViewModel
    
    @State private var isDisplayingError = false
    @State private var spinForward: CGFloat = 0
    @State private var qrImage: UIImage = UIImage(systemName: "xmark.circle") ?? UIImage()
    
    let timer = Timer.publish(every: 30, on: .main, in: .common).autoconnect()
    
    
    var body: some View {
        NavigationPages() {
            
            VStack {
                HStack {
                    VStack(alignment: .leading) {
                        Text("My QR")
                            .font(.system(size: 24, weight: .bold))
                        Text("Welcome back \(authViewModel.user?.getFormattedName() ?? "")")
                            .foregroundColor(Color("Secondary"))
                    }
                    Spacer()
                }
                .padding(.top, 8)
                
                Spacer()
                
                if ticketViewModel.showTicket {
                    VStack(spacing: 0) {
                        VStack(spacing: 0) {
                            HStack(spacing: 0) {
                                Text(ticketViewModel.name)
                                    .font(.custom("SF-Mono", size: ticketViewModel.subText != nil ? 16 : 20))
                                    .kerning(4)
                                if let subTitle = ticketViewModel.subText {
                                    Spacer()
                                    Text("\(String(subTitle.dropLast()))")
                                        .font(.custom("SF-Mono", size: 16))
                                        .kerning(4)
                                    
                                    Text(String(subTitle.last!))
                                        .font(.custom("SF-Mono", size: 16))
                                }
                            }
                            .foregroundColor(Color("HighlightPrimary"))
                            if ticketViewModel.hasDate {
                                HStack {
                                    ForEach(ticketViewModel.date!.indices, id: \.self) { item in
                                        if item != 0 {
                                            Spacer()
                                        }
                                        
                                        Text(ticketViewModel.date![item])
                                            .foregroundColor(Color("Secondary"))
                                            .font(.system(size: 16))
                                    }
                                }
                            }
                        }
                        .padding(24)
                        
                        Button {
                            withAnimation(.linear(duration: 0.6)) {
                                Task{ await ticketViewModel.getTicketData() }
                                authViewModel.getUser()
                            }
                        } label: {
                            Image(uiImage: qrImage)
                                .resizable()
                                .renderingMode(.template)
                                .colorMultiply(Color("Primary"))
                                .frame(width: 175, height: 175)
                                .padding(.bottom, 24)
                        }
                        
                        VStack(spacing: 4) {
                            Text("\(authViewModel.user?.getFormattedName() ?? "")")
                                .font(.system(size: 20))
                            if let status = ticketViewModel.ticketStatus {
                                Text(status)
                                    .font(.custom("IBMPlexMono-Regular", size: 20))
                                    .kerning(5)
                                    .foregroundColor(Color("HighlightPrimary"))
                            }
                        }
                        .padding(.horizontal, 24)
                        .padding(.bottom, 18)
                        
                        HStack {
                            Circle()
                                .frame(width: 30, height: 30)
                                .foregroundColor(Color("Background"))
                                .offset(x: -15)
                            
                            Image("QRCodeDashedLines")
                                .resizable()
                                .frame(height: 2)
                                .frame(maxWidth: .infinity)
                            
                            Circle()
                                .frame(width: 30, height: 30)
                                .foregroundColor(Color("Background"))
                                .offset(x: 15)
                        }
                        .frame(maxWidth: .infinity)
                        .padding(.bottom, 8)
                        
                        Text(ticketViewModel.description)
                            .font(.system(size: 14))
                            .lineLimit(3)
                            .multilineTextAlignment(.center)
                            .frame(height: 40)
                            .padding(.horizontal, 26)
                            .padding(.bottom, 18)
                        
                    }
                    .background {
                        RoundedRectangle(cornerRadius: 8)
                            .foregroundColor(Color("Default"))
                    }
                    .padding(.horizontal, 16)
                } else {
                    
                    Button {
                        withAnimation(.linear(duration: 0.6)) {
                            authViewModel.getUser()
                        }
                    } label: {
                        Image(uiImage: qrImage)
                            .resizable()
                            .renderingMode(.template)
                            .colorMultiply(Color("Primary"))
                            .frame(width: 175, height: 175)
                            .padding(.bottom, 24)
                    }
                    
                    Text(ticketViewModel.description)
                        .font(.system(size: 16))
                        .lineLimit(3)
                        .multilineTextAlignment(.center)
                        .frame(height: 40)
                        .padding(.horizontal, 26)
                        .padding(.bottom, 18)
                }
                
                Spacer()
                
                
                    .onReceive(authViewModel.$errorMessage, perform: { error in
                        guard error != nil && error != "" else {
                            isDisplayingError = false
                            return
                        }
                        isDisplayingError = true
                    })
                    .onReceive(authViewModel.$user) { user in
                        qrImage = (user?.generateQrCodeString() ?? "Not valid").qrImage
                    }
            }
            .onReceive(timer, perform: { _ in
                qrImage = (authViewModel.user?.generateQrCodeString() ?? "Not valid").qrImage
            })
            .navigationTitle("My QR")
            .frame(maxWidth: .infinity)
            .navigationBarHidden(true)
            .padding(.horizontal)
            .padding(.bottom)
            .background(Color("Background"))
        }
    }
}

private extension String {
    
    /// Returns a black and white QR code for this URL.
    var qrImage: UIImage {
        var qrImage = UIImage(systemName: "xmark.circle") ?? UIImage()
        
        guard let qrFilter = CIFilter(name: "CIQRCodeGenerator") else { return qrImage }
        let qrString = self.data(using: .ascii, allowLossyConversion: false)
        
        qrFilter.setValue(qrString, forKey: "inputMessage")
        let qrTransform = CGAffineTransform(scaleX: 10, y: 10)
        let context = CIContext()
        
        
        
        if let outputImage =  qrFilter.outputImage?.transformed(by: qrTransform)  {
            if let image = context.createCGImage(outputImage, from: outputImage.extent) {
                let maskFilter = CIFilter.blendWithMask()
                maskFilter.maskImage = outputImage.applyingFilter("CIColorInvert")
                maskFilter.inputImage = CIImage(
                    color: CIColor(color: .white))
                
                let ciImage = maskFilter.outputImage!
                qrImage = context.createCGImage(ciImage, from: ciImage.extent).map(UIImage.init)!
            }
        }
        
        return qrImage
    }
}


struct MyQrPage_Previews: PreviewProvider {
    static var previews: some View {
        MyQrPage()
    }
}
