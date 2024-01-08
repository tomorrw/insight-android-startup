//
//  PageDisplayView.swift
//  iosApp
//
//  Created by marcjalkh on 08/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//


import SwiftUI
import shared

struct Pages:Hashable, Identifiable{
    var id: Self {
        return self
    }
    let title: String
    let section: SectionDisplayInfo
}

struct PageTabDisplayView: View {
    @State var currentPage: SectionDisplayInfo? = nil
    @Binding var pages: [SectionDisplayInfo]
    @State var cur = 0
    
    var body: some View {
        if !pages.isEmpty {
            VStack{
                TabsHeader(selectedPage: $currentPage, Pages: pages)
                    .onAppear{
                        currentPage = pages.first?.id
                    }
                if currentPage != nil {
                    CurrentPage(section: currentPage!)
                        .padding()
                        .padding(.bottom, 30)
                }
            }
            .frame(maxHeight: .infinity)
            .background(Color("Default"))
            .cornerRadius(20, corners: [.topLeft, .topRight])
        }
    }
    
}

struct TabsHeader: View{
    @Binding var selectedPage: SectionDisplayInfo?
    var Pages: [SectionDisplayInfo]
    @Namespace var namespace
    
    var body: some View{
        HStack {
            HStack(spacing: 20){
                ForEach(Pages){page in
                    Button(action: {
                        selectedPage = page
                    }, label: {
                        Spacer()
                        
                        VStack{
                            Text(page.getInfo().title)
                            if selectedPage == page {
                                Color("HighlightPrimary")
                                    .frame(height: 2)
                                    .matchedGeometryEffect(id: "underline",
                                                           in: namespace,
                                                           properties: .frame)
                            } else{
                                Color.clear.frame(height: 2)
                            }
                        }
                        .animation(.spring(), value: self.selectedPage)
                        
                        Spacer()
                        
                    })
                    .padding(.vertical)
                }
                
            }
        }
        
    }
}

struct CurrentPage: View{
    var section: SectionDisplayInfo
    
    var body: some View{
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

struct PostDisplayView: View{
    @Binding var sections: [SectionDisplayInfo]
    @Binding var actions: [Action]
    
    var body: some View{
        VStack {
            Divider().padding(.bottom, 5)
            
            LazyVStack(alignment: .leading, spacing: 32) {
                ForEach(sections) { section in
                    CurrentPage(section: section)
                }
            }
            
            ActionButtons(actions: actions)
        }
        .padding(.horizontal)
        .padding(.bottom, 30)
    }
}


