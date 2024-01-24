//
//  ToastView.swift
//  iosApp
//
//  Created by Jonathan Benjamin on 14/12/2021.
//

import SwiftUI

struct ToastView: View {
    
    var options: ToastOptions
    
    @State private var isActive: Bool = true
    @State private var fadeInOut: Bool = true
    
    public init(options: ToastOptions) {
        self.options = options
    }
    
    public var body: some View {
        if isActive {
            VStack {
                if options.position == .bottom {
                    Spacer()
                }
                
                HStack {
                    if options.image != nil {
                        options.image
                            .padding([.leading, .top, .bottom])
                            .imageScale(.large)
                    }
                    
                    VStack {
                        Text(options.title)
                            .font(.headline)
                            .padding(.bottom, 0.1)
                            .padding([.leading, .trailing])
                        
                        if options.subtitle != nil {
                            Text(options.subtitle!)
                                .font(.subheadline)
                                .bold()
                                .foregroundColor(.secondary)
                                .padding([.leading, .trailing])
                        }
                    }
                }
                .background(Color(UIColor.secondarySystemBackground))
                .clipShape(Capsule())
                .shadow(color: .black.opacity(0.2), radius: 10)
                .padding()
                .onAppear {
                    if options.duration != nil {
                        withAnimation {
                            fadeInOut.toggle()
                        }
                        DispatchQueue.main.asyncAfter(deadline: .now() + options.duration!, execute: {
                            withAnimation {
                                dismissToast()
                            }
                        })
                    } else {
                        fadeInOut = false
                    }
                }
                .opacity(fadeInOut ? 0 : 1)
                .onTapGesture {
                    if options.dismissible {
                        dismissToast()
                    }
                }
                .accessibilityElement(children: .combine)
                .accessibilityLabel("\(options.title), \(options.subtitle ?? "")")
                
                if options.position == .top {
                    Spacer()
                }
            }
        }
    }
    
    func dismissToast() {
        withAnimation {
            fadeInOut.toggle()
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: {
                isActive = false
            })
        }
    }
}

public struct ToastOptions {
    let image: Image?
    let title: String
    let subtitle: String?
    let position: ToastPosition
    let duration: Double?
    let dismissible: Bool
    /// An object that holds settings for a ToastView.
    ///
    ///  - Parameters:
    ///    - image: The image to display on the left-hand side of the ToastView. Optional.
    ///    - title: The primary string to display on the ToastView.
    ///    - subtitle: The secondary string to display on the ToastView. Optional.
    ///    - position: A `ToastPosition` object that corresponds with the location of the toast on the parent view.
    ///    - duration: The time in seconds to display the ToastView before fading away. Optional. If nil, the ToastView will appear indefinitely.
    ///    - dismissable: If true, the ToastView will fade away when tapped.
    public init(
        image: Image? = nil,
        title: String,
        subtitle: String? = nil,
        position: ToastPosition,
        duration: Double? = nil,
        dismissible: Bool
    ) {
        self.image = image
        self.title = title
        self.subtitle = subtitle
        self.position = position
        self.duration = duration
        self.dismissible = dismissible
    }
}

public enum ToastPosition {
    case top
    case bottom
}
