//
//  NetworkMonitorIndicator.swift
//  iosApp
//
//  Created by Yammine on 11/7/22.
//  Copyright © 2022 tomorrowSARL. All rights reserved.
//

import SwiftUI
import shared

struct NetworkMonitorIndicator: View {
    @StateObject var networkMonitor = NetworkMonitor()
    
    var body: some View {
        if networkMonitor.isDisconnected {
            Text("No Internet Connection!")
                .font(.system(size: 16, weight: .medium))
                .padding(.vertical, 2)
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .background(.red)
        }
    }
}

struct NetworkMonitorIndicator_Previews: PreviewProvider {
    static var previews: some View {
        NetworkMonitorIndicator()
    }
}
