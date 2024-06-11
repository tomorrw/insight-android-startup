//
//  DefaultColors.swift
//  iosApp
//
//  Created by marcjalkh on 28/02/2024.
//  Copyright © 2024 tomorrowSARL. All rights reserved.
//

import SwiftUI
import UiComponents
import DetailPage
struct DefaultColors {
    static var buttonColor = ButtonColors(
        textColor: Color("Default"),
        backgroundColor: Color("Primary"),
        loader:  Color("Default")
    )
    
    static var customTextFieldColor = CustomTextFieldColors(
        boxColor: Color("Secondary"),
        accentColor: Color("HighlightPrimary"),
        success: Color("Success"),
        error: Color("Error")
    )
    
    static var defaultCardColor = CardColors(
        background: Color("Default"),
        foreground: Color("Primary"),
        secondaryText: Color("Secondary")
    )
    
    static var defaultCardBodyColor = CardColors(
        foreground: Color("Primary"),
        secondaryText: Color("Secondary")
    )
    
    static var pageTabDisplayColor = PageTabDisplayColor(
        background: Color("Default"),
        highlitedPage: Color("HighlightPrimary")
    )
    
    static var sessionCardColor = EventCardColors(
        highlightedText: Color("Secondary"),
        background: Color("Default"),
        firstTag: TextTagColor(foreground: Color("Primary"), background: Color("ColoredDefault")),
        secondaryTag: TextTagColor(foreground: Color("Default"),background: .accentColor)
    )
    
    static var sessionCardColorVariation = EventCardColors(
        highlightedText: Color("Secondary"),
        background: Color("Background"),
        firstTag: TextTagColor(foreground: Color("Primary"), background: Color("ColoredDefault")),
        secondaryTag: TextTagColor(foreground: Color("Default"),background: .accentColor)
    )
}
