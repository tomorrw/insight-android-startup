//
//  TicketViewModel.swift
//  iosApp
//
//  Created by marcjalkh on 28/12/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared
import Combine
import KMPNativeCoroutinesAsync

class TicketViewModel: ObservableObject {
    @Published var showTicket: Bool = true
    @Published var hasDate: Bool = false { didSet { displayDate() } }
    @Published var name: String = "Convenire"
    @Published var description: String = "Your Digital Identity"
    @Published var year: String? = nil
    @Published var date: [String]? = nil
    
    private var startDate: Date? = nil
    private var endDate: Date? = nil
    private let dateFormatter = DateFormatter()
    
    init() {
        self.dateFormatter.dateFormat = "yyyy-MM-dd"
        Task{ await self.getTicketData() }
        
    }
    
    @MainActor func getTicketData() async {
        do {
            let result = asyncSequence(for: GetQrCodeInfoUseCase().getTicketInfo())
            
            for try await data in result {
                self.showTicket = data.showTicket
                self.startDate = dateFormatter.date(from: data.startDate?.description() ?? "")
                self.endDate = dateFormatter.date(from: data.endDate?.description() ?? "")
                self.name = data.title
                self.description = data.description_
                self.hasDate = self.startDate != nil && self.endDate != nil
            }
            
        } catch {
            print(error)
        }
    }
    
    
    private func displayDate(){
        if hasDate{
            year = startDate!.getFormatted("yyyy")
            if startDate?.getFormatted("MMMM") == endDate?.getFormatted("MMMM") {
                date = "\(startDate!.getFormatted("MMMM")) \(startDate!.getFormatted("dd")) - \(endDate!.getFormatted("dd"))".map{String($0)}
            }
            else{
                date = "\(startDate!.getFormatted("dd")) \(startDate!.getFormatted("MMM"))  - \(endDate!.getFormatted("dd")) \(endDate!.getFormatted("MMM"))".map{String($0)}
            }
        }
    }
    
}
