//
//  CompaniesToSearchItem.swift
//  iosApp
//
//  Created by Yammine on 4/27/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation
import shared
import SearchableList

extension Array where Element == Company {
    func mapToSearchItems() -> [SearchItem] {
        self.map { $0.toSearchItem() }
    }
}

extension Offer {
    func toOfferSearchItem() -> OfferSearchItem {
        OfferSearchItem(
            id: self.id,
            title: self.title,
            description: self.company.title,
            image: self.image ?? " ",
            postId: self.postId
        )
    }
}

extension Company {
    func toSearchItem() -> SearchItem {
        SearchItem(
            id: self.id,
            title: self.title,
            description: self.objectsClause,
            image: self.image ?? " "
        )
    }
}
