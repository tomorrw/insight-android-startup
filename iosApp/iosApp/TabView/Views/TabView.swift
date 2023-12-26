//
//  TabView.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct TabView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let storyboard = UIStoryboard(name: "HomePage", bundle: Bundle.main)
        let controller = storyboard.instantiateViewController(withIdentifier: "HomePageID")
        return controller
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
