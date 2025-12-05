import FamilyControls
import DeviceActivity
import SwiftUI
import UIKit
import ManagedSettings
import AVFoundation

@available(iOS 16.0, *)
@objc public class AppBlockerSwift: NSObject {
    private let authorizationCenter = AuthorizationCenter.shared
//    private let store = ManagedSettingsStore()
    // private var activitySession: DeviceActivityMonitor?

    @objc public static let shared = AppBlockerSwift()
    private let center = DeviceActivityCenter()
    
    
    @objc public func requestPermission(
        onSuccess: @escaping (Bool) -> Void,
        onError: @escaping (String) -> Void
    ) {
        print("ios: requestPermission")
        
        Task {
            do {
                try await AuthorizationCenter.shared.requestAuthorization(for: .individual)
                await MainActor.run {
                    onSuccess(true)
                }
            } catch let error {
                await MainActor.run {
                    print("ios: error occured: ", error.localizedDescription)
                    onError(error.localizedDescription)
                }
            }
        }
    }
    
    @State private var selectedApps = FamilyActivitySelection()
    @State private var isSelected = false
    @objc public func getSocialMediaUsage(
        days: Int = -7,
        completion: @escaping (SocialMediaUsageResult?) -> Void
    ) async {
        guard AuthorizationCenter.shared.authorizationStatus == .approved else {
            print("permission denied")
            requestPermission { Bool in
                
            } onError: { err in
                print("permission denied ", err)
                return
            }
            
            return
        }
        
        let store = ManagedSettingsStore()
        if store.shield.applications == nil {
            store.shield.applications = selectedApps.applicationTokens
        }
        
    let report = Text("Hello").familyActivityPicker(isPresented: $isSelected, selection: $selectedApps)
        
        let host = UIHostingController(rootView: report)
        host.view.frame = CGRect(x: -1000, y: -1000, width: 100, height: 100)
        UIApplication.shared.windows.first?.rootViewController?.view.addSubview(host.view)
        
        startMonitoring(selectedApps: selectedApps, threshold: 0)
    }
    
    let activityMonitor = DeviceActivityCenter()
    func startMonitoring(selectedApps: FamilyActivitySelection, threshold: TimeInterval) {
        let threshold: TimeInterval = 3600
        let calendar = Calendar.current
        let event = DeviceActivityEvent(
            applications: selectedApps.applicationTokens,
            threshold: DateComponents(second: 60)
        )
        let date = Date()
        let hour = calendar.component(.hour, from: date)
        let minute = calendar.component(.minute, from: date)

        let events: [DeviceActivityEvent.Name: DeviceActivityEvent] = [
            .init("daily_limit"): event
        ]

        let allDaySchedule = DeviceActivitySchedule(
            intervalStart: DateComponents(hour: hour, minute: minute),
            intervalEnd: DateComponents(hour: hour + 1, minute: minute),
            repeats: false
        )

        do {
            try activityMonitor.startMonitoring(
                .init(rawValue: "com.yourapp.activity"),
                during: allDaySchedule,
                events: events
            )
            print("apps", $selectedApps)
            print("Device activity monitoring started successfully.")
        } catch {
            print("Error starting monitoring: \(error)")
        }
    }
}

struct AppUsageData {
    let appName: String
    let usageTime: Double
    let category: String
    
    var formattedTime: String {
        let hours = Int(usageTime) / 60
        let minutes = Int(usageTime) % 60
        return hours > 0 ? "\(hours)h \(minutes)m" : "\(minutes)m"
    }
}

//@objcMembers
public class SocialMediaUsageResult: NSObject, Codable {
    public let title: String
    public let subtitle: String
    public let footer: String
    public let apps: [String]

    init(title: String, subtitle: String, footer: String, apps: [String]) {
        self.title = title
        self.subtitle = subtitle
        self.footer = footer
        self.apps = apps
    }
}

@available(iOS 16.0, *)
extension DeviceActivityReport.Context {
    static let socialMediaGuilt = Self("socialMediaGuilt")   // ← Must be identical everywhere
}
