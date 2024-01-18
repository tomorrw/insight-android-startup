//
//  PageDisplayView.swift
//  iosApp
//
//  Created by marcjalkh on 08/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//


import SwiftUI

struct PageTabDisplayView: View {
    @State var currentPage: PagePresentationModel? = nil
    @Binding var pages: [PagePresentationModel]
    
    @Environment(\.sessionCardColors) var backgroundColor: SessionCardColors
    
    var body: some View {
        if !pages.isEmpty {
            VStack{
                TabsHeader(selectedPage: $currentPage, Pages: pages)
                    .onAppear{
                        currentPage = pages.first?.id
                    }
                if currentPage != nil {
                    ForEach(currentPage!.sections) { section in
                        SectionDisplayPage(section: section)
                            .frame(maxHeight: .infinity)
                            .environment(\.sessionCardColors, SessionCardColors(background: Color("Background")))
                            .padding(.horizontal)
                            .padding(.bottom, 30)
                    }
                }
            }
            .background(Color("Default"))
            .cornerRadius(20, corners: [.topLeft, .topRight])
            .gesture(horizontalDrag(next: { currentPage = pages[nextPage(1)] }, previous: {currentPage = pages[nextPage(-1)]}))
        }
    }
    
    private func nextPage(_ index : Int)-> Int {
        if currentPage != nil && !pages.isEmpty {
            let index = (pages.firstIndex(of: currentPage!) ?? 0) + index
            return max(min(index, pages.count - 1), 0)
        }
        return 0
    }
}

struct TabsHeader: View{
    @Binding var selectedPage: PagePresentationModel?
    var Pages: [PagePresentationModel]
    @Namespace var namespace
    
    var body: some View{
        
        HStack {
            ForEach(Pages){ page in
                Button(action: {
                    selectedPage = page
                }, label: {
                    Spacer()
                    
                    VStack{
                        Text(page.title)
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
