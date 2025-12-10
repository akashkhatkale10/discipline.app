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
    @ObservedObject var state: AppBlockerState
    
    @State private var blockedApps: [ApplicationToken] = [
        //AppItem(name: "all apps blocked", icon: "square.grid.2x2.fill")
    ]
    
    @State private var unblockedApps: [ApplicationToken] = []
    
    var body: some View {
        NavigationStack {
            VStack(alignment: .leading, spacing: 0) {
                Text("apps blocked")
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
                            count: blockedApps.count,
                            limit: 50,
                            apps: state.activitySelection.applicationTokens,
                            showsRemove: false
                        )
                        
                        AppWebsiteSection(
                            title: "blocked websites",
                            count: blockedApps.count,
                            limit: 50,
                            domains: state.activitySelection.webDomainTokens,
                            showsRemove: false
                        )

                        Button(action: { onAddClick() }) {
                            HStack {
                                Image(systemName: "plus")
                                Text("add an app / website")
                                    .customFont(.medium, 12)
                            }
                            .foregroundColor(.white)
                            .padding(.horizontal, 14)
                            .frame(height: 24)
                            .background(Color(hex: "CF414B"))
                            .clipShape(Capsule())
                        }
                        .buttonStyle(TapBounce())
                    }
                }
                .padding(.horizontal, 16)
                .padding(.top, 30)

                // MARK: - Fixed Footer Buttons
                
                GradientButton(
                    action: {
                    
                    }, startIcon: "play.fill"
                )
                .padding(.horizontal, 16)
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
    let count: Int
    let limit: Int
    let apps: Set<ApplicationToken>
    let showsRemove: Bool
    var onRemove: ((AppItem) -> Void)? = nil
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            SectionHeader(title: title, count: count, limit: limit)
            
            ForEach(Array(apps), id: \.self) { token in
                Label(token)
                    .labelStyle(AppTokenIconLabelStyle())
                    .frame(height: 40)
            }
        }
    }
}

@available(iOS 16.0, *)
struct AppWebsiteSection: View {
    
    let title: String
    let count: Int
    let limit: Int
    let domains: Set<WebDomainToken>
    let showsRemove: Bool
    var onRemove: ((AppItem) -> Void)? = nil
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            SectionHeader(title: title, count: count, limit: limit)
            
            ForEach(Array(domains), id: \.self) { token in
                Label(token)
                    .labelStyle(AppTokenIconLabelStyle())
                    .frame(height: 40)
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
    let count: Int
    let limit: Int
    
    var body: some View {
        HStack {
            Text(title)
                .customFont(.medium, 14)
                .foregroundColor(.white)
            
            Text("(\(count) / \(limit))")
                .foregroundColor(.white.opacity(0.3))
                .customFont(.medium, 14)
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
