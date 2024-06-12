//
//  NavigateTo.swift
//  iosApp
//
//  Created by marcjalkh on 11/09/2023.
//  Copyright © 2023 tomorrowSARL. All rights reserved.
//
import SwiftUI

/*============================================
 ! This Navigation method is a temporary fix !
 ===========================================*/

//Replaces NavigationLink(destination:View,label:View)
struct NavigateTo<Label: View, Destination: View>: View {
    internal init(destination: @escaping () -> Destination, label: @escaping () -> Label) {
        self.destination = destination
        self.label = label
    }
    
    
    @ViewBuilder let destination: () -> Destination
    @ViewBuilder let label: () -> Label
    var body: some View {
        NavigationLink( destination: {
            newScreenNavigationStyle(destination: destination)
        },
            label: label
        )
        .buttonStyle(.plain)
    }
}

//Replaces MavigationLink(isActive:Bool,destination:View,label:View)
struct NavigateToWithActive<Label: View, Destination: View>: View {
    
    @ViewBuilder let destination: () -> Destination
    @ViewBuilder let label:  () -> Label
    @Binding var isActive: Bool
    var body: some View {
        NavigationLink(isActive: $isActive,
                       destination: {
            newScreenNavigationStyle(destination: destination)
        },
                       label: label
        )
        .buttonStyle(.plain)
    }
}

//Replaces NavigationLink(label:String,destination:View)
struct NavigateTxtTo<Destination: View>: View {
    
    let label:  String
    @ViewBuilder let destination: () -> Destination
    var body: some View {
        NavigationLink( label, destination:{
            newScreenNavigationStyle(destination: destination)})
        .buttonStyle(.plain)
    }
}

//Replaces NavigationLink(Label:String,destination:View,isActive:Binding<Bool>
struct NavigateEmptyTo<Destination: View>: View {
    
    let label:  String
    @ViewBuilder let destination: () -> Destination
    @Binding var isActive: Bool
    
    var body: some View {
        NavigationLink( label, destination:
            newScreenNavigationStyle(destination: destination)
        , isActive: $isActive)
        .buttonStyle(.plain)
    }
}

struct newScreenNavigationStyle<Destination: View>: View{
    @ViewBuilder let destination: () -> Destination
    @Environment(\.presentationMode) private var presentationMode
    @State var poppedOnce : Bool = false
    
    var body: some View{
        destination()
            .onReceive(ViewModel.sharedVm.viewDismissalModePublisher) { shouldPop in
                if (shouldPop && !poppedOnce) {
                    self.presentationMode.wrappedValue.dismiss()
                    }
            }
            .onAppear{
                self.poppedOnce = false
            }
            .onDisappear{
                self.poppedOnce = true
            }
    }
}

import Foundation
import Combine
class ViewModel: ObservableObject {
    var viewDismissalModePublisher = PassthroughSubject<Bool, Never>()
    
    static let sharedVm = ViewModel()
    
    func shouldPopThis(){
        self.shouldPopView = true
    }
    
    private var shouldPopView = false {
        didSet {
            viewDismissalModePublisher.send(shouldPopView)
        }
    }
    
}
