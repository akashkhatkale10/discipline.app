//
//  ScreenTimeReportExtension.swift
//  ScreenTimeReportExtension
//
//  Created by Akash Khatkale on 03/12/25.
//

import DeviceActivity
import SwiftUI

extension DeviceActivityReport.Context {
    static let socialMediaGuilt = Self("socialMediaGuilt")   // ← Must be identical everywhere
}

@main
struct ScreenTimeReportExtension: DeviceActivityReportExtension {
    var body: some DeviceActivityReportScene {
        SocialMediaGuiltScene()
    }
}

struct SocialMediaGuiltScene: DeviceActivityReportScene {
    var content: (GuiltConfiguration) -> GuiltReportView = { config in
        GuiltReportView(config: config)
    }
    
    let context = DeviceActivityReport.Context.socialMediaGuilt
    
    typealias Configuration = GuiltConfiguration
        
    // Required: Associated type Content = GuiltReportView
    typealias Content = GuiltReportView

    
    func makeConfiguration(representing data: DeviceActivityResults<DeviceActivityData>) async -> GuiltConfiguration {
        var guilt = await GuiltConfiguration.compute(from: data)
        let result = SocialMediaUsageResult(
            title: "Social media",
            subtitle: guilt.subtitle,
            footer: guilt.footer,
            apps: guilt.apps
        )
        let shared = UserDefaults.standard
        print(shared.integer(forKey: "demo"))
        return guilt
    }
}

// MARK: - Configuration (holds your guilt data)
struct GuiltConfiguration {
    let subtitle: String
    let footer: String
    let apps: [String]
    
    static func compute(from data: DeviceActivityResults<DeviceActivityData>) async -> GuiltConfiguration {
        print("inside compute")
        var totalSeconds: TimeInterval = 0
        var socialApps: Set<String> = []
        
        let socialBundleIDs = [
            "net.whatsapp.WhatsApp"
        ]
        
        for await activityData in data {
            for await segment in activityData.activitySegments {
                for await category in segment.categories {
                    
                    socialApps.insert(category.category.localizedDisplayName!)
                    for await appActivity in category.applications {
                        let bundle = appActivity.application.bundleIdentifier!
                        let name = appActivity.application.localizedDisplayName
                            ?? bundle.components(separatedBy: ".").last?.capitalized
                            ?? "Social App"
                        if socialBundleIDs.contains(bundle) {
                            totalSeconds += appActivity.totalActivityDuration
                        }
                        
//                        let name = appActivity.application.localizedDisplayName
//                            ?? bundle.components(separatedBy: ".").last?.capitalized
//                            ?? "Social App"
//                        socialApps.insert(bundle)
                    }
                }
            }
        }
        
        let hours = Int(totalSeconds / 3600)
        let minutes = Int((totalSeconds.truncatingRemainder(dividingBy: 3600)) / 60)
        let subtitle = hours > 0 ? "\(hours)h \(minutes)m" : "\(minutes)m"
        
        let daysLost = Int((totalSeconds / 3600 * 365) / 24)
        let footer = daysLost > 0
            ? "This is \(daysLost) days of your life gone in a year"
            : "You're still doomed — just slowly"
        
        print("hours ", hours)
        print("apps ", socialApps)
        
        return GuiltConfiguration(
            subtitle: subtitle,
            footer: footer,
            apps: Array(socialApps).sorted()
        )
    }
}

struct GuiltReportView: View {
    let config: GuiltConfiguration
    
    var body: some View {
    
        VStack(spacing: 30) {
            Spacer()
            
            Text(config.subtitle)
                .font(.system(size: 72, weight: .black, design: .rounded))
                .foregroundColor(.red)
                .multilineTextAlignment(.center)
            
            Text(config.footer)
                .font(.title2)
                .foregroundColor(.gray)
                .multilineTextAlignment(.center)
                .padding(.horizontal)
            
            if !config.apps.isEmpty {
                Text("Main culprits: \(config.apps.joined(separator: ", "))\(config.apps.count > 3 ? "..." : "")")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
            
            Spacer()
        }
    }
}


struct SocialMediaUsageResult: Codable {
    let title: String
    let subtitle: String
    let footer: String
    let apps: [String]
}
