//
//  DetailPageViewModel.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared

class DetailPageViewModel: ObservableObject {    
    @Published var image: String = ""
    @Published var imagePinIcon: String? = nil
    @Published var infoImage: String = ""
    @Published var title: String = ""
    @Published var description: String = ""
    @Published var headerDesign: HeaderDesign = .contact
    @Published var errorMessage: String? = nil
    @Published var isLoading: Bool = false
        
    enum HeaderDesign {
        case contact
        case detailPage
    }
}

struct InfoContent: Hashable, HasInfo {
    let title: String
    let description: String
    let imageUrl: String?
}

struct VideoContent: Hashable, HasInfo {
    let title: String
    let description: String
    let videoUrl: String
}

struct EventContent: Hashable, HasInfo {
    let title: String
    let description: String
    let sessions: [Session]
}

struct SpeakersContent: Hashable, HasInfo {
    let title: String
    let description: String
    let speakers: [Speaker]
}

enum SectionDisplayInfo: Hashable, Identifiable {
    var id: Self {
        return self
    }
    
    func getInfo() -> HasInfo {
        switch self {
        case let .info(data): return data
        case let .video(data): return data
        case let .sessions(data): return data
        case let .speakers(data): return data
        }
    }
    
    case info(InfoContent)
    case video(VideoContent)
    case sessions(EventContent)
    case speakers(SpeakersContent)
}


struct PagePresentationModel: Hashable, Identifiable {
    var id: Self {
        return self
    }
    var title: String
    var sections: [SectionDisplayInfo]
}
