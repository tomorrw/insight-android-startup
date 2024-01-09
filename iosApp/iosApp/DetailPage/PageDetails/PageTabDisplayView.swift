//
//  PageDisplayView.swift
//  iosApp
//
//  Created by marcjalkh on 08/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//


import SwiftUI

struct PageTabDisplayView: View {
    @State var currentPage: SectionDisplayInfo? = nil
    @Binding var pages: [SectionDisplayInfo]
    
    @Environment(\.sessionCardBackgroundColor) var backgroundColor: Color
    
    var body: some View {
        if !pages.isEmpty {
            VStack{
                TabsHeader(selectedPage: $currentPage, Pages: pages)
                    .onAppear{
                        currentPage = pages.first?.id
                    }
                if currentPage != nil {
                    SectionDisplayPage(section: currentPage!)
                        .frame(maxHeight: .infinity)
                        .environment(\.sessionCardBackgroundColor, Color("Background"))
                        .padding(.horizontal)
                        .padding(.bottom, 30)
                }
            }
            .background(Color("Default"))
            .cornerRadius(20, corners: [.topLeft, .topRight])
            .gesture(horizontalDrag)
        }
    }
    
    private func nextPage(_ index : Int)-> Int {
        if currentPage != nil && !pages.isEmpty {
            let index = (pages.firstIndex(of: currentPage!) ?? 0) + index
            return max(min(index, pages.count - 1), 0)
        }
        return -1
    }
    
    var horizontalDrag: some Gesture {
        DragGesture(coordinateSpace: .local)
            .onEnded { value in
                if value.translation.width < -50 {
                    withAnimation {
                        currentPage = pages[nextPage(1)]
                    }
                } else if value.translation.width > 50 {
                    withAnimation {
                        currentPage = pages[nextPage(-1)]
                    }
                }
            }
    }
    
}

struct TabsHeader: View{
    @Binding var selectedPage: SectionDisplayInfo?
    var Pages: [SectionDisplayInfo]
    @Namespace var namespace
    
    var body: some View{
        
        HStack {
            ForEach(Pages){ page in
                Button(action: {
                    selectedPage = page
                }, label: {
                    Spacer()
                    
                    VStack{
                        Text(page.getInfo().title)
                            .padding(.vertical, 3)
                        Group{
                            if selectedPage == page {
                                Color("HighlightPrimary")
                                    .frame(height: 2)
                                    .matchedGeometryEffect(id: "underline", in: namespace, properties: .frame)
                            } else{
                                Color.clear.frame(height: 2)
                            }
                        }
                        .animation(.spring(), value: self.selectedPage)
                    }
                    
                    Spacer()
                })
                .padding(.vertical)
            }
            
        }
    }
}

struct Pages:Hashable, Identifiable{
    var id: Self { return self }
    let title: String
    let section: SectionDisplayInfo
}
