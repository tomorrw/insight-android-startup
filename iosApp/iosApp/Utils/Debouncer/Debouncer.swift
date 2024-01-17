//
//  Debouncer.swift
//  iosApp
//
//  Created by Yammine on 5/30/23.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//

import Foundation

///
/// this is for debouncing functionality
/// it is instantiated with the debounce time interval
/// then fires the handler once after the interval is done
///
public class Debouncer {
    
    private let timeInterval: TimeInterval
    private var timer: Timer?
    public var renewCounter: Int = 0
    
    typealias Handler = () -> Void
    var handler: Handler?
    
    init(timeInterval: TimeInterval) {
        self.timeInterval = timeInterval
    }
    
    public func renewInterval() {
        renewCounter += 1
        timer?.invalidate()
        timer = Timer.scheduledTimer(withTimeInterval: timeInterval, repeats: false, block: { [weak self] (timer) in
            self?.timeIntervalDidFinish(for: timer)
        })
    }
    
    @objc private func timeIntervalDidFinish(for timer: Timer) {
        guard timer.isValid else {
            return
        }
        handler?()
        handler = nil
        renewCounter = 0
    }
    
}
