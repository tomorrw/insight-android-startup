//
//  MyQrPresentationModel.swift
//  iosApp
//
//  Created by marcjalkh on 01/02/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import shared

class MyQrPresentationModel{
    @Published var userName: String? = nil
    @Published var qrCodeString: String = "Not valid"
    var user: User? = nil
    
    init(user: User? = nil) {
        self.user = user
        self.userName = self.user?.getFormattedName()
        self.qrCodeString = self.user?.generateQrCodeString() ?? "Not Valid"
    }
    
    func load(user: User? = nil) {
        self.user = user
    }
}

class EmptyTicketPresentationModel: MyQrPresentationModel{
    @Published var description: String = "Your Digital Identity"
    
    init(user: User?, description: String) {
        super.init(user: user)
        self.description = description
    }
}

class TicketPresentationModel: MyQrPresentationModel{
    @Published var rightTitle: String? = nil
    @Published var leftTitle: String = "Convenire"
    @Published var subText: [String]? = nil
    @Published var ticketStatus: String? = nil
    @Published var description: String = "Your Digital Identity"
    
    init(user:User?, rightTitle: String? = nil, leftTitle: String, subText: [String]? = nil, ticketStatus: String? = nil,description: String) {
        super.init(user: user)
        self.rightTitle = rightTitle
        self.leftTitle = leftTitle
        self.subText = subText
        self.ticketStatus = ticketStatus
        self.description = description
    }
}
