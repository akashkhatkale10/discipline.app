import FamilyControls
import DeviceActivity
import SwiftUI
import UIKit
import ManagedSettings
import AVFoundation

@objc public class ScreenTimeManagerSwift: NSObject {
    // private let center = DeviceActivityCenter()
    // private let authCenter = AuthorizationCenter.shared
    //
    // private let authorizationCenter = AuthorizationCenter.shared
    // private let store = ManagedSettingsStore()
    // private var activitySession: DeviceActivityMonitor?


    @objc public func requestPermission(completion: @escaping (Bool) -> Void) {
        print("starting requesting functions")
        // let status = AVCaptureDevice.authorizationStatus(for: .video)
        //
        // switch status {
        // case .authorized:
        //     completion(true) // Already granted
        // case .notDetermined:
        //     AVCaptureDevice.requestAccess(for: .video) { granted in
        //         DispatchQueue.main.async {
        //             completion(granted) // Shows popup here
        //         }
        //     }
        // case .denied, .restricted:
        //     completion(false) // Permission denied or restricted
        // @unknown default:
        //     completion(false)
    }

    @objc public func startMonitoring() {

    }

    @objc public func stopMonitoring() {

    }

    // Convert bundle IDs to Set<Application> (for Kotlin)
    @objc public func applicationsFromBundleIds(bundleIds: [String]) -> NSSet {
//        let apps = bundleIds.compactMap { Application(bundleIdentifier: $0) }
        return NSSet(array: [])
    }

    // Get demo apps (expand later to real query)
    @objc public class func demoBlockableApps() -> [String: String] {
        return [
            "com.instagram.Instagram": "Instagram",
            "com.tiktok.TikTok": "TikTok",
            "com.facebook.Facebook": "Facebook",
            "com.snapchat.Snapchat": "Snapchat",
            "com.google.YouTube": "YouTube", // Fixed bundle ID
            "com.twitter.twitter": "X"  // Fixed bundle ID
        ]
    }
}
