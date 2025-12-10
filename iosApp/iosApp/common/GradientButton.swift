//
//  GradientButton.swift
//  iosApp
//
//  Created by Akash Khatkale on 10/12/25.
//
import SwiftUI

@available(iOS 16.0, *)
struct GradientButton: View {
    let action: () -> Void
    let startIcon: String

    var body: some View {
        Button(action: action) {
            HStack(spacing: 12) {
                Image(systemName: startIcon)
                    .foregroundColor(.white)
                    .font(.system(size: 14, weight: .semibold))

                Text("Start a focus session")
                    .foregroundColor(.white)
                    .customFont(.medium, 14)
            }
            .frame(maxWidth: .infinity)
            .frame(height: 40)
            .background(
                LinearGradient(
                    colors: [
                        Color(hex: "1A9597"), // teal
                        Color(hex: "0B80E6") // blue
                    ],
                    startPoint: .leading,
                    endPoint: .trailing
                )
            )
            .clipShape(
                RoundedRectangle(cornerRadius: 14)
            )
        }
        .overlay(
            RoundedRectangle(cornerRadius: 14)
                .stroke(
                    LinearGradient(
                        colors: [
                            Color(hex: "52C0C1"), // teal
                            Color(hex: "0B80E6")
                        ],
                        startPoint: .leading,
                        endPoint: .trailing
                    ),
                    lineWidth: 1
                )
        )
    }
}
