//
//  HomePageTabBarController.swift
//  Dentiflow
//
//  Created by Joe El Hachem on 1/4/23.
//

import UIKit
import SwiftUI
import Resolver

class HomePageTabBarController: UITabBarController, UITabBarControllerDelegate {
    private var previousIndex = 0

    override func viewDidLoad() {
        super.viewDidLoad()
        self.delegate = self
    }

    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        if tabBarController.selectedIndex == previousIndex{
            ViewModel.sharedVm.shouldPopThis()
//// example of functionality that could be done here:
//            if tabBarController.selectedIndex == 2 {
//
//            }
        }
        if #available(iOS 16, *) {
            NavMethodsNew.shared.popToRoot()
        }
        previousIndex = tabBarController.selectedIndex
    }
}

class HomeViewHostingController: UIHostingController<HomePage> {
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder, rootView: HomePage())
    }
}




class MyQrViewHostingController: UIHostingController<AnotherPage> {
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder, rootView: AnotherPage())
    }
}

