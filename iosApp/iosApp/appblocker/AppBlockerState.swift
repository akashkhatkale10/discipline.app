//
//  AppBlockerState.swift
//  iosApp
//
//  Created by Akash Khatkale on 08/12/25.
//
import SwiftUI
import FamilyControls

@available(iOS 16.0, *)
class AppBlockerState: ObservableObject {
    static let shared = AppBlockerState()
    
    @Published var activitySelection = FamilyActivitySelection.init(includeEntireCategory: true)
    @Published var isPickerPresented: Bool = false
    @Published var showSheet: Bool = true
}

