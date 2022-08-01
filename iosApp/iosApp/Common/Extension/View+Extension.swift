//
//  View+Extension.swift
//  iosApp
//
//  Created by 菅原勝也 on 2022/08/01.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

extension View {
    func onFirstAppear(perform action: @escaping () -> Void) -> some View {
        modifier(FirstAppearModifier(action: action))
    }
}

private struct FirstAppearModifier: ViewModifier {
    let action: () -> Void

    @State
    private var appeared = false

    func body(content: Content) -> some View {
        content.onAppear {
            if !appeared {
                appeared = true
                action()
            }
        }
    }
}
