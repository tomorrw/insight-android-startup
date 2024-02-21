//
//  SearchListModel.swift
//  iosApp
//
//  Created by marcjalkh on 09/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import shared

struct SearchCategory:  Equatable, Comparable, Identifiable {
    static func < (lhs: SearchCategory, rhs: SearchCategory) -> Bool {
        return lhs.text == rhs.text
    }
    
    static func == (lhs: SearchCategory, rhs: SearchCategory) -> Bool {
        return lhs.text == rhs.text
    }
    
    init(_ text: String, image: String? = nil) {
        self.text = text
        self.iconImage = image
        self.id = UUID()
    }
    
    let id: UUID
    let text: String
    let iconImage: String?
}

class SearchItem: Identifiable, Equatable {
    
    static func == (lhs: SearchItem, rhs: SearchItem) -> Bool {
        return lhs.id == rhs.id
    }
    
    let id: String
    let title: String
    let description: String
    let image: String?
    var category: SearchCategory? = nil
    
    init(id: String, title: String, description: String, image: String?, category: SearchCategory? = nil) {
        self.id = id
        self.title = title
        self.description = description
        self.image = image
        self.category = category
    }
    func getSearchableString() -> String {
        return title + " " + description
    }
}

struct CategoryListItem: Equatable, Comparable, Identifiable {
    static func < (lhs: CategoryListItem, rhs: CategoryListItem) -> Bool {
        return lhs.category == rhs.category
    }
    
    static func == (lhs: CategoryListItem, rhs: CategoryListItem) -> Bool {
        return lhs.category == rhs.category
    }
    
    let id: UUID = UUID()
    let category: SearchCategory
    let items: [SearchItem]
}
