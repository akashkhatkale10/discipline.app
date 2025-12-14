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
    var content: (PhoneUsageStatsModel) -> PhoneUsageView = { config in
        PhoneUsageView(config: config)
    }
    
    let context = DeviceActivityReport.Context.socialMediaGuilt
    
    typealias Configuration = PhoneUsageStatsModel
        
    // Required: Associated type Content = GuiltReportView
    typealias Content = PhoneUsageView

    
    func makeConfiguration(representing data: DeviceActivityResults<DeviceActivityData>) async -> PhoneUsageStatsModel {
        let usage = await PhoneUsageStatsModel.compute(from: data)
        return usage
    }
}

// MARK: - Configuration (holds your guilt data)
struct PhoneUsageItemModel {
    let title: String
    let value: String
    let subtitle: AttributedString
    let backgroundColor: Color
    let borderColor: Color
    let titleIcon: String
    let apps: [String]
}
struct PhoneUsageStatsModel {
    let numberOfDaysInYears: String
    let items: [PhoneUsageItemModel]
    
    static func compute(from data: DeviceActivityResults<DeviceActivityData>) async -> PhoneUsageStatsModel {

        // MARK: - Accumulators
        var socialSeconds: TimeInterval = 0
        var socialApps: Set<String> = []

        var appDurations: [String: TimeInterval] = [:]  // bundle → duration
        var appNames: [String: String] = [:]

        var appNotifications: [String: Int] = [:] // bundle → notification count
        var pickups: Int = 0

        // MARK: - Parse DeviceActivity Data
        for await activityData in data {
            
            
            for await segment in activityData.activitySegments {
                pickups += segment.totalPickupsWithoutApplicationActivity
                for await category in segment.categories {

                    // Capture social apps (or filter using your own social list)
                    for await appActivity in category.applications {
                        
                        pickups += appActivity.numberOfPickups
                        let bundle = appActivity.application.bundleIdentifier ?? "unknown"
                        let name = appActivity.application.localizedDisplayName

                        let duration = appActivity.totalActivityDuration
                        let notifications = appActivity.numberOfNotifications
                        
                        appDurations[bundle, default: 0] += duration
                        appNotifications[bundle, default: 0] += notifications
                        appNames[bundle] = name

                        // Social usage total
                        if category.category.localizedDisplayName?.lowercased().contains("social") == true {
                            socialApps.insert(appActivity.application.localizedDisplayName ?? "")
                        }
                        if category.category.localizedDisplayName?.lowercased().contains("social") == true {
                            socialSeconds += duration
                        }
                    }
                }
            }
        }

        // MARK: - Helpers
        func formatHours(_ seconds: TimeInterval) -> String {
            let h = Int(seconds / 3600)
            let m = Int((seconds.truncatingRemainder(dividingBy: 3600)) / 60)
            return h > 0 ? "\(h)h \(m)m" : "\(m)m"
        }

        func computeDaysLost(_ seconds: TimeInterval) -> Int {
            let hoursInYear = (seconds / 3600) * 365
            return Int(hoursInYear / (24 * 7 ))
        }

        func colorForSeverity(_ seconds: TimeInterval) -> Color {
            let hours = seconds / 3600
            if hours > 25 { return Color(hex: "#CF414B") }  // alarming
            if hours > 10 { return Color(hex: "#FDBB2D") }  // moderate
            return Color(hex: "#339D52")                   // normal
        }

        func bgFor(_ color: Color) -> Color {
            color.opacity(0.3)
        }

        // MARK: - Social Media Item
        let socialDaysLost = computeDaysLost(socialSeconds)
        let socialColor = colorForSeverity(socialSeconds)

        let socialItem = PhoneUsageItemModel(
            title: "social media usage in last 7 days",
            value: formatHours(socialSeconds),
            subtitle: makeSubtitleString(first: "this is", second: socialDaysLost, third: "of your life gone in a year"),
            backgroundColor: bgFor(socialColor),
            borderColor: socialColor,
            titleIcon: "",
            apps: Array(socialApps)
        )

        // MARK: - Most Used App
        let mostUsedBundle = appDurations.max(by: { $0.value < $1.value })?.key
        let mostUsedSeconds = appDurations[mostUsedBundle ?? ""] ?? 0
        let mostUsedName = appNames[mostUsedBundle ?? ""] ?? "App"
        
        let mostUsedDaysLost = computeDaysLost(mostUsedSeconds)
        let mostUsedColor = colorForSeverity(mostUsedSeconds)

        let mostUsedItem = PhoneUsageItemModel(
            title: "most used app in last 7 days",
            value: formatHours(mostUsedSeconds),
            subtitle: makeSubtitleString(first: "this is", second: mostUsedDaysLost, third: "of your life gone in a year"),
            backgroundColor: bgFor(mostUsedColor),
            borderColor: mostUsedColor,
            titleIcon: mostUsedBundle ?? "",
            apps: [mostUsedName]
        )

        // MARK: - Most Disturbing App (Notifications)
        let mostDisturbingBundle = appNotifications.max(by: { $0.value < $1.value })?.key
        let mostDisturbingName = appNames[mostDisturbingBundle ?? ""] ?? "App"
        let notifsCount = appNotifications[mostDisturbingBundle ?? ""] ?? 0

        let mostDisturbingColor: Color = notifsCount > 100
            ? Color(hex: "#CF414B")
            : notifsCount > 40
                ? Color(hex: "#FDBB2D")
                : Color(hex: "#339D52")

        let disturbingItem = PhoneUsageItemModel(
            title: "most disturbing app in last 7 days",
            value: "\(notifsCount) notifs",
            subtitle: "",
            backgroundColor: bgFor(mostDisturbingColor),
            borderColor: mostDisturbingColor,
            titleIcon: mostDisturbingBundle ?? "",
            apps: [mostDisturbingName]
        )

        // MARK: - Phone Pickups Yesterday
        let pickupsColor: Color = pickups > 120
            ? Color(hex: "#CF414B")
            : pickups > 60
                ? Color(hex: "#FDBB2D")
                : Color(hex: "#339D52")

        let pickupsItem = PhoneUsageItemModel(
            title: "phone pickups in last 7 days",
            value: "\(pickups)",
            subtitle: "",
            backgroundColor: bgFor(pickupsColor),
            borderColor: pickupsColor,
            titleIcon: "",
            apps: []
        )

        // MARK: - Final Return
        let totalDuration = appDurations.values.reduce(0, +)
        return PhoneUsageStatsModel(
            numberOfDaysInYears: "\(computeDaysLost(totalDuration))",
            items: [
                socialItem,
                pickupsItem,
                mostUsedItem,
                disturbingItem
            ]
        )
    }
}

func makeSubtitleString(first: String, second: Int, third: String) -> AttributedString {
    var str = AttributedString("\(first) ")
    str.foregroundColor = Color(.white).opacity(0.3)
    str.font = Font.custom("Montserrat-Medium", size: 10)
        
    var boldRed = AttributedString("\(second) days")
    boldRed.foregroundColor = Color(hex: "CF414B")
    boldRed.font = Font.custom("Montserrat-Medium", size: 10)
    str.append(boldRed)
    
    var rest = AttributedString(" \(third)")
    rest.foregroundColor = Color(.white).opacity(0.3)
    rest.font = Font.custom("Montserrat-Medium", size: 10)
    str.append(rest)
    
    return str
}

extension PhoneUsageStatsModel {
    func toString() -> String {
        var result = "Days in Year: \(numberOfDaysInYears)\n\n"

        for item in items {
            result += """
            Title: \(item.title)
            Value: \(item.value)
            Subtitle: \(item.subtitle)
            Icon: \(item.titleIcon)
            Apps: \(item.apps.joined(separator: ", "))
            \n\n
            """
        }

        return result
    }
}


actor AppIconFetcher {

    static let shared = AppIconFetcher()

    struct LookupResponse: Decodable {
        struct Result: Decodable {
            let artworkUrl512: String?
        }
        let results: [Result]
    }

    /// Fetch an App’s icon URL using the App Store lookup API.
    /// Returns: URL? of 512px App Icon
    func fetchIconURL(bundleId: String) async -> URL? {
        let urlString = "https://itunes.apple.com/lookup?bundleId=\(bundleId)"
        guard let url = URL(string: urlString) else { return nil }

        do {
            let (data, _) = try await URLSession.shared.data(from: url)
            let decoded = try JSONDecoder().decode(LookupResponse.self, from: data)

            return decoded.results.first?.artworkUrl512.flatMap(URL.init(string:))
        } catch {
            print("AppIconFetcher error: \(error)")
            return nil
        }
    }

    /// Download the actual image data
    func fetchImage(bundleId: String) async -> UIImage? {
        guard let url = await fetchIconURL(bundleId: bundleId) else { return nil }

        do {
            let (data, _) = try await URLSession.shared.data(from: url)
            return UIImage(data: data)
        } catch {
            print("Failed to load icon data: \(error)")
            return nil
        }
    }
}
