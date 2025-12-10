//
//  AppsBlockedBS.swift
//  iosApp
//
//  Created by Akash Khatkale on 08/12/25.
//

import SwiftUI
import FamilyControls
import ManagedSettings


@available(iOS 16.0, *)
struct AppItem: Identifiable {
    let id = UUID()
    let name: String
    let icon: String      // SF Symbol for now
}
@available(iOS 16.0, *)
struct AppsBlockedBottomSheet: View {
    let onAddClick: () -> Void
    let dismissClick: () -> Void
    @ObservedObject var state: AppBlockerState
    
    @State private var blockedApps: [ApplicationToken] = [
        //AppItem(name: "all apps blocked", icon: "square.grid.2x2.fill")
    ]
    
    @State private var unblockedApps: [ApplicationToken] = []
    
    var body: some View {
        NavigationStack {
            VStack(alignment: .leading, spacing: 0) {
                Text("🚫  apps blocked")
                    .customFont(.semiBold, 16)
                    .foregroundColor(.white)
                    .padding(.top, 30)
                    .padding(.horizontal, 16)

                Text("these apps / websites will be blocked during your focus time")
                    .customFont(.medium, 14)
                    .foregroundColor(.white.opacity(0.3))
                    .padding(.top, 24)
                    .padding(.horizontal, 16)

                ScrollView(.vertical, showsIndicators: false) {
                    VStack(alignment: .leading, spacing: 24) {
                        // Blocked Apps
                        AppListSection(
                            title: "blocked apps",
                            subtitle: "your blocked apps will be shown here",
                            count: state.activitySelection.applicationTokens.count,
                            limit: 50,
                            apps: state.activitySelection.applicationTokens,
                            showsRemove: false) { token in
                                state.activitySelection.applicationTokens.remove(token)
                            }
                        
                        AppWebsiteSection(
                            title: "blocked websites",
                            subtitle: "your blocked websites will be shown here",
                            count: state.activitySelection.webDomainTokens.count,
                            limit: 50,
                            domains: state.activitySelection.webDomainTokens,
                            showsRemove: false,
                            onRemove: { token in
                                state.activitySelection.webDomainTokens.remove(token)
                            }
                        )
                        .padding(.top, 30)

                    }
                }
                .padding(.horizontal, 16)
                .padding(.top, 50)
                
                GradientButton(
                    action: {
                        onAddClick()
                    },
                    startIcon: "plus",
                    title: "add an app / website"
                )
                .padding(.horizontal, 16)
                
                SecondaryButton(
                    action: {
                        dismissClick()
                    },
                    startIcon: "",
                    title: "cancel"
                )
                .padding(.horizontal, 16)
                .padding(.top, 20)
            }
            .background(Color(hex: "121212"))
        }
        .presentationDetents([.large])
        .presentationDragIndicator(.visible)
    }
}

// MARK: - Section Header + List
@available(iOS 16.0, *)
struct AppListSection: View {
    let title: String
    let subtitle: String
    let count: Int
    let limit: Int
    let apps: Set<ApplicationToken>
    let showsRemove: Bool
    var onRemove: ((ApplicationToken) -> Void)? = nil
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            SectionHeader(title: title, subtitle: subtitle, count: count, limit: limit)
            
            ForEach(Array(apps), id: \.self) { token in
                HStack(spacing: 14) {
                    Label(token)
                        .labelStyle(AppTokenIconLabelStyle())
                        .frame(height: 40)
                    
                    SmallIconButton(
                        borderColor: Color(hex: "C74545").opacity(0.5),
                        backgroundColor: Color(hex: "C74545"),
                        icon: "xmark",
                        action: {
                            if onRemove != nil {
                                onRemove!(token)
                            }
                        }
                    )
                }
            }
        }
    }
}

@available(iOS 16.0, *)
struct AppWebsiteSection: View {
    
    let title: String
    let subtitle: String
    let count: Int
    let limit: Int
    let domains: Set<WebDomainToken>
    let showsRemove: Bool
    var onRemove: ((WebDomainToken) -> Void)? = nil
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            SectionHeader(title: title, subtitle: subtitle, count: count, limit: limit)
            
            ForEach(Array(domains), id: \.self) { token in
                HStack(spacing: 14) {
                    Label(token)
                        .labelStyle(AppTokenIconLabelStyle())
                        .frame(height: 40)
                    
                    SmallIconButton(
                        borderColor: Color(hex: "C74545").opacity(0.5),
                        backgroundColor: Color(hex: "C74545"),
                        icon: "xmark",
                        action: {
                            if onRemove != nil {
                                onRemove!(token)
                            }
                        }
                    )
                }
            }
        }
    }
}


@available(iOS 16.0, *)
struct AppTokenIconLabelStyle: LabelStyle {
    func makeBody(configuration: Configuration) -> some View {
        HStack(spacing: 8) {
            configuration.icon
                .frame(width: 32, height: 32)
                .clipShape(RoundedRectangle(cornerRadius: 8))

            configuration.title
                .foregroundColor(.white)
        }
//        .padding(.vertical, 12)
        .frame(width: .infinity)
        .padding(.horizontal, 14)
        .background(Color.white.opacity(0.02))
        .overlay(
            RoundedRectangle(cornerRadius: 14)
                .stroke(Color.white.opacity(0.1), lineWidth: 2)
        )
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }
}


// MARK: - Section Header
@available(iOS 16.0, *)
struct SectionHeader: View {
    let title: String
    let subtitle: String
    let count: Int
    let limit: Int
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                Text(title)
                    .customFont(.medium, 14)
                    .foregroundColor(.white)
                
                Spacer()
                
                Text("(\(count) / \(limit))")
                    .foregroundColor(.white.opacity(0.3))
                    .customFont(.medium, 14)
            }
            
            if count == 0 {
                Text(subtitle)
                    .customFont(.medium, 12)
                    .foregroundColor(.white.opacity(0.3))
                    .padding(.top, 6)
            }
        }
    }
}

// MARK: - Single App Row
@available(iOS 16.0, *)
struct AppRow: View {
    
    let app: AppItem
    let showsRemove: Bool
    var onRemove: (() -> Void)? = nil
    
    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: app.icon)
                .frame(width: 24, height: 24)
                .foregroundColor(.white)
                .background(Color.blue)
                .clipShape(RoundedRectangle(cornerRadius: 8))
            
            Text(app.name)
                .customFont(.regular, 14)
                .foregroundColor(.white)
            
            Spacer()
            
            if showsRemove {
                Button(action: { onRemove?() }) {
                    Image(systemName: "xmark.circle.fill")
                        .font(.title3)
                        .foregroundColor(.red.opacity(0.95))
                }
            }
        }
        .padding(.horizontal, 14)
        .padding(.vertical, 12)
        .background(Color.white.opacity(0.02))
        .overlay(
            RoundedRectangle(cornerRadius: 14)
                .stroke(Color.white.opacity(0.1), lineWidth: 2)
        )
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }
}


struct TapBounce: ButtonStyle {
    var scale: CGFloat = 0.92
    var duration: CGFloat = 0.12

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? scale : 1)
            .animation(.easeInOut(duration: duration), value: configuration.isPressed)
    }
}


@available(iOS 16.0, *)
struct GradientButton: View {
    let action: () -> Void
    let startIcon: String
    let title: String

    var body: some View {
        Button(action: action) {
            HStack(spacing: 12) {
                if !startIcon.isEmpty {
                    Image(systemName: startIcon)
                        .foregroundColor(.white)
                        .font(.system(size: 14, weight: .semibold))
                }

                Text(title)
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
                        lineWidth: 2
                    )
            )
        }
        .buttonStyle(TapBounce())
        
    }
}


@available(iOS 16.0, *)
struct SecondaryButton: View {
    let action: () -> Void
    let startIcon: String
    let title: String

    var body: some View {
        Button(action: action) {
            HStack(spacing: 12) {
                if !startIcon.isEmpty {
                    Image(systemName: startIcon)
                        .foregroundColor(.white)
                        .font(.system(size: 14, weight: .semibold))
                }
                

                Text(title)
                    .foregroundColor(.white)
                    .customFont(.medium, 14)
            }
            .frame(maxWidth: .infinity)
            .frame(height: 40)
            .background(
                LinearGradient(
                    colors: [
                        Color(hex: "FFFFFF").opacity(0.02), // teal
                        Color(hex: "FFFFFF").opacity(0.02) // blue
                    ],
                    startPoint: .leading,
                    endPoint: .trailing
                )
            )
            .clipShape(
                RoundedRectangle(cornerRadius: 14)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 14)
                    .stroke(
                        LinearGradient(
                            colors: [
                                Color(hex: "FFFFFF").opacity(0.1), // teal
                                Color(hex: "FFFFFF").opacity(0.1)
                            ],
                            startPoint: .leading,
                            endPoint: .trailing
                        ),
                        lineWidth: 2
                    )
            )
        }
        .buttonStyle(TapBounce())
    
        
    }
}

@available(iOS 16.0, *)
struct SmallIconButton: View {
    var size: CGFloat = 18
    let borderColor: Color
    let backgroundColor: Color
    let icon: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            ZStack {
                Circle()
                    .fill(backgroundColor) // inner red
                    .overlay(
                        Circle()
                            .stroke(borderColor, lineWidth: 1)
                    )

                if !icon.isEmpty {
                    Image(systemName: icon)
                        .font(.system(size: 10, weight: .bold))
                        .foregroundColor(.white)
                }
                
            }
            .frame(width: size, height: size)
        }
        .buttonStyle(TapBounce())
        .onTapGesture {
            action()
        }
    }
}
