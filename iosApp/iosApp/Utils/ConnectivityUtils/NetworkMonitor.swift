//
//  NetworkMonitor.swift
//  iosApp
//
//  Created by Yammine on 11/7/22.
//  Copyright © 2022 tomorrowSARL. All rights reserved.
//

import SwiftUI
import Network

final class NetworkMonitor: ObservableObject {
    let monitor = NWPathMonitor()
    let queue = DispatchQueue(label: "Monitor")
     
    @Published var isDisconnected = false
     
    init() {
        monitor.pathUpdateHandler =  { [weak self] path in
            DispatchQueue.main.async {
                self?.isDisconnected = path.status != .satisfied
            }
        }
        monitor.start(queue: queue)
    }
}
