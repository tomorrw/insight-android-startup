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
    @Published var qrCodeString: String = "No user"
    var user: User? = nil{
        didSet{
            self.userName = user?.getFormattedName()
            self.qrCodeString = user?.generateQrCodeString() ?? "No user"
        }
    }
    
    func loadUser(_ user: User) {
        self.user = user
    }
}

class EmptyTicketPresentationModel: MyQrPresentationModel{
    @Published var description: String = "Your Digital Identity"
    
    init(description: String) {
        self.description = description
    }
}

class TicketPresentationModel: MyQrPresentationModel{
    @Published var rightTitle: String? = nil
    @Published var leftTitle: String = "Convenire"
    @Published var subText: [String]? = nil
    @Published var ticketStatus: String? = nil
    @Published var description: String = "Your Digital Identity"

    init(rightTitle: String? = nil, leftTitle: String, subText: [String]? = nil, ticketStatus: String? = nil,description: String) {
        self.rightTitle = rightTitle
        self.leftTitle = leftTitle
        self.subText = subText
        self.ticketStatus = ticketStatus
        self.description = description
    }
}
