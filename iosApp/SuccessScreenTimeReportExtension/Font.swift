//
//  Font.swift
//  iosApp
//
//  Created by Akash Khatkale on 06/12/25.
//

import SwiftUI

enum FontWeight {
    case light
    case regular
    case medium
    case semiBold
    case bold
    case extraBold
}

extension Font {
    static let customFont: (FontWeight, CGFloat) -> Font = { fontType, size in
        switch fontType {
        case .light:
            Font.custom("Montserrat-Light", size: size)
        case .regular:
            Font.custom("Montserrat-Regular", size: size)
        case .medium:
            Font.custom("Montserrat-Medium", size: size)
        case .semiBold:
            Font.custom("Montserrat-SemiBold", size: size)
        case .bold:
            Font.custom("Montserrat-Bold", size: size)
        case .extraBold:
            Font.custom("Montserrat-ExtraBold", size: size)
        }
    }
}

extension Text {
    func customFont(_ fontWeight: FontWeight? = .regular, _ size: CGFloat? = nil) -> Text {
        return self.font(.customFont(fontWeight ?? .regular, size ?? 16))
    }
}
