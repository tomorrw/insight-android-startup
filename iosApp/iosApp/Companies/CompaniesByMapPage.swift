//
//  CompaniesByMapPage.swift
//  iosApp
//
//  Created by marcjalkh on 25/09/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Resolver

struct CompaniesByMapPage: View {
    @InjectedObject private var vm: CompanyByMapPageViewModel
    @State var image: String = ""
    @State var floorMapImage: String = ""
    @State var arrowCGRect: CGRect = CGRectZero
    let itemHeight: CGFloat = 75
    
    var body: some View {
        VStack(spacing: 0){
            if vm.isLoading && vm.data.isEmpty {
                CustomLoader()
            } else {
                VStack {
                    ZStack {
                        UrlImageView(urlString: floorMapImage)
                            .frame(maxWidth: .infinity)
                            .aspectRatio(contentMode: .fit)
                            .id(floorMapImage)
                        
                        UrlImageView(urlString: image, placeholderImage: "locating-map")
                            .frame(maxWidth: .infinity)
                            .aspectRatio(contentMode: .fit)
                            .id(image)
                    }
                    .padding(.horizontal)
                    
                    ZStack(alignment: .topLeading) {
                        GeometryReader { scrollGeometry in
                            ScrollView {
                                ForEach(vm.data, id: \.self.id) {  company in
                                    GeometryReader { itemGeometry in
                                        let itemFrame = itemGeometry.frame(in: CoordinateSpace.global)
                                        
                                        NavigateTo {
                                            CompanyPage(id: company.id)
                                        } label: {
                                            PlainListCard(image: company.image, title: company.title, description: company.objectsClause)
                                                .padding(.horizontal, 15)
                                        }
                                        .onChange(of: itemFrame) { newItemFrame in
                                            if !newItemFrame.contains(CGPoint(x: newItemFrame.midX, y: arrowCGRect.midY)) { return }
                                            
                                            image = company.floorMapGroup?.image ?? ""
                                            floorMapImage = company.floorMapGroup?.floorImage ?? ""
                                        }
                                    }
                                    .frame(height: itemHeight)
                                }
                                
                                // this allows the user to scroll the last element to the top
                                Spacer().frame(height: (scrollGeometry.size.height - itemHeight).coerceIn(0, scrollGeometry.size.height))
                            }
                        }
                        
                        GeometryReader { arrowGeometry in
                            let arrowRect = arrowGeometry.frame(in: CoordinateSpace.global)
                            
                            Image(systemName: "arrow.right")
                                .foregroundColor(Color("HighlightPrimary"))
                                .frame(height: arrowRect.height)
                                .onChange(of: arrowRect, perform: { arrowCGRect = $0 })
                            
                        }
                        .frame(height: itemHeight)
                    }
                }
                .navigationTitle("Exhibitions By Map")
                .navigationBarTitleDisplayMode(.inline)
                .background(Color("Background"))
                .refreshable { Task{ await vm.refreshData () }}
                
            }
        }
        .task { await vm.getCompanies() }
    }
}

struct CompaniesByMapPage_Previews: PreviewProvider {
    static var previews: some View {
        CompaniesByMapPage()
    }
}
