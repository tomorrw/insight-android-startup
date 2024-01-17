//
//  Date.swift
//  iosApp
//
//  Created by marcjalkh on 28/12/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation

extension Date{
    
    func getFormatted(_ format: String) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = format
        return dateFormatter.string(from: self)
    }

}

extension String{
    
    func formatDate(_ initialFormat: String) -> Date? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = initialFormat
        return dateFormatter.date(from: self)
    }
}
