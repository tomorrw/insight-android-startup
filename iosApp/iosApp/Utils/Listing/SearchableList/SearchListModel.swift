//
//  SearchListModel.swift
//  iosApp
//
//  Created by marcjalkh on 09/01/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import shared

struct SearchCategory: Hashable, Equatable, Comparable, Identifiable {
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

struct SearchItem: Identifiable, Equatable, Hashable {
    let id: String
    let title: String
    let description: String
    let image: String?
    var category: SearchCategory? = nil
    
    func getSearchableString() -> String {
        return title + " " + description
    }
}

struct CategoryListItem: Hashable, Equatable, Comparable, Identifiable {
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
