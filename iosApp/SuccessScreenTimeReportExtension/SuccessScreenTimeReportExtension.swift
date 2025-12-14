//
//  SuccessScreenTimeReportExtension.swift
//  iosApp
//
//  Created by Akash Khatkale on 14/12/25.
//
import DeviceActivity
import FamilyControls
import ManagedSettings
import SwiftUI

extension DeviceActivityReport.Context {
    static let successScreen = Self("successScreen")   // ← Must be identical everywhere
}

@main
struct SuccessScreenTimeReportExtension: DeviceActivityReportExtension {
    var body: some DeviceActivityReportScene {
        SuccessScreenTimeReport()
    }
}

struct SuccessScreenTimeReport: DeviceActivityReportScene {
    var content: (SuccessScreenTimeModel) -> SuccessScreenView = { config in
        SuccessScreenView(config: config)
    }
    
    let context = DeviceActivityReport.Context.successScreen
    
    typealias Configuration = SuccessScreenTimeModel
        
    // Required: Associated type Content = GuiltReportView
    typealias Content = SuccessScreenView

    
    func makeConfiguration(representing data: DeviceActivityResults<DeviceActivityData>) async -> SuccessScreenTimeModel {
        let usage = await SuccessScreenTimeModel.compute(from: data)
        return usage
    }
}

struct PhoneUsageItemModel {
    let title: String
    let value: String
    let subtitle: AttributedString
    let backgroundColor: Color
    let borderColor: Color
    let titleIcon: String
    let apps: [String]
}

struct SuccessScreenTimeModel {
    let appsUsed: [Application]
    let startTime: String
    let endTime: String
    let items: [PhoneUsageItemModel]
    
    
    static func compute(from data: DeviceActivityResults<DeviceActivityData>) async -> SuccessScreenTimeModel {
        var appsUsedSet = Set<Application>()
        var totalPickups = 0
        var totalNotifications = 0
        var totalUsageSeconds: TimeInterval = 0

        var startDate: Date?
        var endDate: Date?

        for await activityData in data {
            for await segment in activityData.activitySegments {
               // Start & End time
               if startDate == nil {
                   startDate = segment.dateInterval.start
               }
               endDate = segment.dateInterval.end

               // Usage duration
               totalUsageSeconds += segment.totalActivityDuration

               // Apps used (for timeline icons)
               for await category in segment.categories {
                    for await app in category.applications {
                        totalNotifications += app.numberOfNotifications
                        
                        if app.numberOfPickups > 0 {
                            totalPickups += app.numberOfPickups
                            appsUsedSet.insert(app.application)
                        }
                    }
               }
           }
        }

        // MARK: - Formatting helpers
        let timeFormatter: DateFormatter = {
           let df = DateFormatter()
           df.dateFormat = "dd MMM yyyy, hh:mm a"
           return df
        }()

        let startTime = startDate.map { timeFormatter.string(from: $0) } ?? "--"
        let endTime = endDate.map { timeFormatter.string(from: $0) } ?? "--"

        let totalMinutes = Int(totalUsageSeconds / 60)

        // MARK: - UI Items

        let items: [PhoneUsageItemModel] = [
           PhoneUsageItemModel(
               title: "phone pickups",
               value: "\(totalPickups)",
               subtitle: "",
               backgroundColor: Color.white.opacity(0.08),
               borderColor: Color.white.opacity(0.2),
               titleIcon: "",
               apps: []
           ),
           PhoneUsageItemModel(
               title: "total notifications",
               value: "\(totalNotifications)",
               subtitle: "",
               backgroundColor: Color.white.opacity(0.08),
               borderColor: Color.white.opacity(0.2),
               titleIcon: "",
               apps: []
           ),
           PhoneUsageItemModel(
               title: "phone usage",
               value: "\(totalMinutes) mins",
               subtitle: "",
               backgroundColor: Color.white.opacity(0.08),
               borderColor: Color.white.opacity(0.2),
               titleIcon: "",
               apps: []
           )
        ]

        return SuccessScreenTimeModel(
           appsUsed: Array(appsUsedSet),
           startTime: startTime,
           endTime: endTime,
           items: items
        )
    }
}
