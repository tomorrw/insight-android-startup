//
//  SwipePages.swift
//  iosApp
//
//  Created by Said on 14/09/2022.
//  Copyright © 2022 tomorrowSARL. All rights reserved.
//

import SwiftUI

// for this to work you have to attach it
// to an Hstack inside a geometry reader
// and pass the proxy width as offsetwidth
// background offset at 0 for first page
// can be used with PageIndicator (scroll down)
struct SwipePages: ViewModifier {
    @Environment(\.layoutDirection) var layoutDirection: LayoutDirection
    @Binding var backgroundOffset: CGFloat
    @State var temporaryOffset: CGFloat = 0
    @GestureState private var isDragging: Bool = false
    @State var gestureState: GestureStatus = .idle
    
    var timed: Bool = true
    
    var offsetWidth: CGFloat
    var pagesCount: Int
    
    var offset: CGFloat {
        -(backgroundOffset * offsetWidth)
    }
    
    var scrollingDirection: CGFloat {
        layoutDirection == .rightToLeft ? -1 : 1
    }
    
    let timer = Timer.publish(every: 5, on: .main, in: .common).autoconnect()
    
    func body(content: Content) -> some View {
        content
            .offset(x: offset)
            .offset(x: temporaryOffset)
            .highPriorityGesture(
                DragGesture()
                    .updating($isDragging) { _, isDragging, _ in
                        isDragging = true
                    }
                    .onChanged { v in
                        onDragChange(v)
                    }
                    .onEnded { v in
                        onDragEnded(v)
                    }

            )
            .onChange(of: gestureState) { state in
                guard state == .started else { return }
                gestureState = .active
            }
            .onChange(of: isDragging) { value in
                if value, gestureState != .started {
                    gestureState = .started
                } else if !value, gestureState != .ended {
                    gestureState = .cancelled
                    temporaryOffset = 0
                }
            }
            .onReceive(timer) { _ in
                if timed { switchPage() }   
            }
    }
    
    func onDragChange(_ value: DragGesture.Value) {
        guard gestureState == .started || gestureState == .active else { return }
        withAnimation(.easeInOut) {
            temporaryOffset = value.translation.width * scrollingDirection
        }
    }

    func onDragEnded(_ value: DragGesture.Value) {
        gestureState = .ended
        withAnimation(.easeInOut(duration: 0.3)) {
            temporaryOffset = 0
            if value.translation.width * scrollingDirection > 50 && backgroundOffset > 0 {
                backgroundOffset -= 1
            } else if value.translation.width * scrollingDirection < -50 && backgroundOffset < CGFloat(pagesCount - 1) {
                backgroundOffset += 1
            }
        }
    }

    enum GestureStatus: Equatable {
        case idle
        case started
        case active
        case ended
        case cancelled
    }
    
    func switchPage() {
        if isDragging { return }
        withAnimation {
            if backgroundOffset < CGFloat(pagesCount - 1) {
                backgroundOffset += 1
            } else {
                backgroundOffset = 0
            }
        }
    }
}

extension View {
    func swipePages(backgroundOffset: Binding<CGFloat>, offsetWidth: CGFloat, pagesCount: Int, timed: Bool = true) -> some View {
        modifier(SwipePages(backgroundOffset: backgroundOffset, timed: timed, offsetWidth: offsetWidth, pagesCount: pagesCount))
    }
}

// can only be used with SwipePages
struct PageIndicator: View {
    var pagesCount: Int
    var backgroundOffset: CGFloat // same value passed to swipePages
    
    var body: some View {
        HStack {
            ForEach(0..<pagesCount, id: \.self) { index in
                Circle()
                    .frame(width: 8, height: 8)
                    .foregroundColor(CGFloat(index) == backgroundOffset ? Color("HighlightPrimary") : Color("UnHighlighted"))
            }
        }
    }
}
