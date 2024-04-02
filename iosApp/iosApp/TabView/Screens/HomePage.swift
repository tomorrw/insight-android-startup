//
//  HomePage.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import Resolver

struct HomePage: View {
    @InjectedObject var vm: HomePageViewModel
    @State private var isDisplayingError = false
    @State var adWidth: CGFloat = 0
    
    var body: some View {
        NavigationPages() {
            VStack(alignment: .leading) {
                Text("Home")
                    .font(.system(size: 24, weight: .bold))
                    .padding(.top, 8)
                Text("Browse announcements & updates")
                    .foregroundColor(Color("Secondary"))
                    .padding(.bottom, 8)
                
                ZStack {
                    if (vm.data == nil && vm.isLoading) {
                        VStack(spacing: 16) {
                            ForEach(0..<2, id:\.self) { _ in
                                RoundedRectangle(cornerRadius: 16)
                                    .fill(.gray.opacity(0.3))
                                    .frame(maxWidth: .infinity)
                                    .frame(height: 160)
                            }
                            
                            Spacer()
                        }
                        .frame(maxHeight: .infinity)
                        .padding(.vertical, 24)
                    }
                    else if vm.data?.isEmpty() ?? false && !vm.isLoading {
                        EmptyStateView (
                            title: "Nothing here.",
                            text: "Stay tuned for more!",
                            buttonText: "Reload",
                            buttonAction: {
                                Task { await vm.refresh() }
                            }
                        )
                    }
                    
                    ScrollView(showsIndicators: false) {
                        VStack(spacing: 48) {
                            if let highlightedPost = vm.data?.highlightedPost.getDataIfLoaded() {
                                PostsView(posts: [highlightedPost])
                            }
                            
                            if let todaysSpeaker = vm.data?.todaySpeakers.getDataIfLoaded() as? [Speaker],
                               todaysSpeaker.isEmpty == false {
                                VStack(alignment: .leading, spacing: 16) {
                                    HStack {
                                        Text("Featured Speakers")
                                            .font(.system(size: 20, weight: .medium))
                                        Spacer()
                                        NavigateTxtTo(label: "SHOW ALL", destination: { NavigationLazyView(SpeakersPage()) })
                                            .foregroundColor(Color("HighlightPrimary"))
                                            .font(.system(size: 14))
                                    }
                                    SpeakersHorizontalView(speakers: todaysSpeaker)
                                }
                            }
                            
                            if
                                let ads = vm.data?.highlightedAds.getDataIfLoaded() as? [Ad],
                                !ads.isEmpty
                            {
                                VStack(alignment: .leading, spacing: 16) {
                                    IterableCarousel(ads, itemWidth: $adWidth) { ad in
                                        CarouselItem(
                                            item: CarouselItemModel(
                                                imageUrl: ad.image,
                                                ctaUrl: ad.url
                                            ),
                                            width: adWidth
                                        )
                                    }
                                }
                                .frame(maxWidth: .infinity)
                            }
                            
                            if let sessions = vm.data?.upcomingSessions.getDataIfLoaded() as? [Session],
                               sessions.isEmpty == false {
                                VStack(alignment: .leading, spacing: 16) {
                                    HStack {
                                        Text("Upcoming Sessions")
                                            .font(.system(size: 20, weight: .medium))
                                        Spacer()
                                        NavigateTxtTo(label: "SHOW ALL") {
                                            SessionsPage()
                                                .navigationTitle("Lectures")
                                                .navigationBarTitleDisplayMode(.inline)
                                        }
                                        .foregroundColor(Color("HighlightPrimary"))
                                        .font(.system(size: 14))
                                    }
                                    SessionsVerticalView(sessions: sessions)
                                }
                                
                                if
                                    let ads = vm.data?.ads.getDataIfLoaded() as? [Ad],
                                    !ads.isEmpty
                                {
                                    VStack(alignment: .leading, spacing: 16) {
                                        IterableCarousel(ads.reversed(), itemWidth: $adWidth) { ad in
                                            CarouselItem(
                                                item: CarouselItemModel(
                                                    imageUrl: ad.image,
                                                    ctaUrl: ad.url
                                                ),
                                                width: adWidth
                                            )
                                        }
                                    }
                                    .frame(maxWidth: .infinity)
                                }
                            }
                            
                            if
                                let posts = vm.data?.posts.getDataIfLoaded() as? [Post],
                                !posts.isEmpty
                            {
                                VStack(alignment: .leading, spacing: 16) {
                                    Text("Updates")
                                        .font(.system(size: 20, weight: .medium))
                                    
                                    PostsView(posts: posts)
                                }
                            }
                        }
                        .padding(.top, 16)
                        .padding(.bottom, 24)
                    }
                    .refreshable { Task{ await vm.refresh() }}
                    .cornerRadius(16, corners: .topRight)
                    .cornerRadius(16, corners: .topLeft)
                    .frame(maxHeight: .infinity)
                    .onAppear { Task { await vm.getData() }}
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
            .padding(.horizontal)
            .navigationTitle("Home")
            .navigationBarHidden(true)
            .frame(maxWidth: .infinity)
            .background(Color("Background"))
        }
    }
}

struct HomePage_Previews: PreviewProvider {
    static var previews: some View {
        HomePage()
    }
}
