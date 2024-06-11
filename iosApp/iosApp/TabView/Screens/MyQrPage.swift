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
import UiComponents

struct MyQrPage: View {
    @InjectedObject var ticketViewModel: TicketViewModel
    
    @State private var isDisplayingToast = false
    @State private var isDisplayingError = false
    @State private var spinForward: CGFloat = 0
    @State private var qrImage: UIImage = UIImage(systemName: "xmark.circle") ?? UIImage()
    @State private var showPopUp: Bool = false
    let timer = Timer.publish(every: 30, on: .main, in: .common).autoconnect()
    
    var body: some View {
        NavigationPages() {
            ZStack{
                
                VStack {
                    HStack {
                        VStack(alignment: .leading) {
                            Text("My QR")
                                .font(.system(size: 24, weight: .bold))
                            Text("Welcome back \(ticketViewModel.pageData.userName ?? "")")
                                .foregroundColor(Color("Secondary"))
                        }
                        Spacer()
                    }
                    .padding(.top, 8)
                    
                    
                    Spacer()
                    
                    if let ticket = ticketViewModel.pageData as? TicketPresentationModel {
                        TicketView(
                            ticket: TicketModel(
                                title: ticket.leftTitle,
                                rightTitle: ticket.rightTitle,
                                subTitle: ticket.subText,
                                qrImage: {
                                    Image(uiImage: qrImage)
                                        .resizable()
                                        .background {
                                            RoundedRectangle(cornerRadius: 8)
                                                .foregroundColor(Color.white)
                                        }
                                },
                                buttonAction: { self.ticketViewModel.getData() },
                                longPressAction: { self.showPopUp.toggle() },
                                holder: ticketViewModel.pageData.userName,
                                status: ticket.ticketStatus,
                                description: ticket.description
                            ),
                            style: TicketStyle(
                                titleFont: "IBMPlexMono-Regular",
                                foregroundColor: Color("Primary"),
                                titleColor: Color("HighlightPrimary"),
                                subTitleColor: Color("Secondary"),
                                StatusColor: Color("HighlightPrimary"),
                                backgroundColor: Color("Background"),
                                ticketColor: Color("Default")
                            )
                        )
                    }
                    
                    else if let emptyTicketInfo = ticketViewModel.pageData as? EmptyTicketPresentationModel {
                        Button { } label: {
                            Image(uiImage: qrImage)
                                .resizable()
                                .background {
                                    RoundedRectangle(cornerRadius: 8)
                                        .foregroundColor(Color.white)
                                }
                                .frame(width: 200, height: 200)
                                .padding(.bottom, 24)
                        }
                        .simultaneousGesture(
                            LongPressGesture()
                                .onEnded { _ in
                                    self.showPopUp.toggle()
                                }
                        )
                        .highPriorityGesture(
                            TapGesture()
                                .onEnded { _ in
                                    self.ticketViewModel.getData()
                                }
                        )
                        .buttonStyle(.plain)
                        
                        Text(emptyTicketInfo.description)
                            .font(.system(size: 16))
                            .lineLimit(3)
                            .multilineTextAlignment(.center)
                            .frame(height: 40)
                            .padding(.horizontal, 26)
                            .padding(.bottom, 18)
                    }
                    else{
                        Button {
                            withAnimation(.linear(duration: 0.6)) {}
                        } label: {
                            Image(uiImage: qrImage)
                                .resizable()
                                .background {
                                    RoundedRectangle(cornerRadius: 8)
                                        .foregroundColor(Color.white)
                                }
                                .frame(width: 200, height: 200)
                                .padding(.bottom, 24)
                        }
                        .simultaneousGesture(
                            LongPressGesture()
                                .onEnded { _ in
                                    self.showPopUp.toggle()
                                }
                        )
                        .highPriorityGesture(
                            TapGesture()
                                .onEnded { _ in
                                    self.ticketViewModel.getData()
                                }
                        )
                        .buttonStyle(.plain)
                    }
                    
                    Spacer()
                        .onReceive(ticketViewModel.pageData.$qrCodeString) { qrString in
                            qrImage = ticketViewModel.pageData.qrCodeString.qrImage
                        }
                        .onReceive(ticketViewModel.$errorMessage, perform: { error in
                            guard error != nil && error != "" else {
                                isDisplayingError = false
                                return
                            }
                            isDisplayingError = true
                        })
                }
                .onReceive(timer, perform: { _ in
                    self.ticketViewModel.pageData.qrCodeString = self.ticketViewModel.pageData.user?.generateQrCodeString() ?? "Not valid"
                })
                .onAppear{ ticketViewModel.getData() }
                .navigationTitle("My QR")
                .frame(maxWidth: .infinity)
                .navigationBarHidden(true)
                .padding(.horizontal)
                .padding(.bottom)
                .background(Color("Background"))
                
                if showPopUp{
                    VStack{
                        Image(uiImage: qrImage)
                            .resizable()
                            .background {
                                RoundedRectangle(cornerRadius: 8)
                                    .foregroundColor(Color.white)
                            }
                            .frame(width: UIScreen.main.bounds.size.width, height: UIScreen.main.bounds.size.width)
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(Color("Background").opacity(0.75))
                    .onTapGesture { self.showPopUp.toggle() }
                }
            }
            .onReceive(ticketViewModel.$websocketMessage, perform: { newMsg in
                if !newMsg.isEmpty { isDisplayingToast = true }
            })
            .overlay {
                if isDisplayingToast {
                    ToastView(options: ToastOptions(
                        image: ticketViewModel.websocketStatus ? Image("Waving") : Image("Cross") ,
                        title: ticketViewModel.websocketMessage,
                        position: ToastPosition.top,
                        duration: 7,
                        dismissible: true,
                        onDismiss: { isDisplayingToast.toggle() })
                    )
                }
            }
            .onAppear{
                ticketViewModel.startListening()
            }
            .onDisappear{
                ticketViewModel.stopListening()
            }
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
            if context.createCGImage(outputImage, from: outputImage.extent) != nil {
                let maskFilter = CIFilter.blendWithMask()
                maskFilter.maskImage = outputImage.applyingFilter("CIColorInvert")
                maskFilter.inputImage = CIImage(
                    color: CIColor(color: UIColor(red: 0.067, green: 0.247, blue: 0.404, alpha: 1.0)))
                
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
