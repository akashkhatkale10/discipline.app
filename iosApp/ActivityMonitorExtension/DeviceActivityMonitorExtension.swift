//
//  DeviceActivityMonitorExtension.swift
//  ActivityMonitorExtension
//
//  Created by Akash Khatkale on 04/12/25.
//

import DeviceActivity
import ManagedSettings
import MobileCoreServices
import SwiftUI
import FamilyControls

// Optionally override any of the functions below.
// Make sure that your class name matches the NSExtensionPrincipalClass in your Info.plist.
class DeviceActivityMonitorExtension: DeviceActivityMonitor {
    let store = ManagedSettingsStore(named: ManagedSettingsStore.Name("demoRestrictions"))
    
    override func intervalDidStart(for activity: DeviceActivityName) {
//        super.intervalDidStart(for: activity)
//        let applicationTokens = activity.applicationTokens
//        let categoryTokens = selection.categoryTokens
//        let webTokens = selection.webDomainTokens
//        store.shield.applications = applicationTokens.isEmpty ? nil : applicationTokens
//        store.shield.applicationCategories = categoryTokens.isEmpty ? nil : .specific(categoryTokens)
//        store.shield.webDomains = webTokens.isEmpty ? nil : webTokens
//        store.application.blockedApplications = selection.applications
        store.application.denyAppRemoval = true
        store.application.denyAppInstallation = true
        store.passcode.lockPasscode = true
        // Handle the start of the interval.
    }
    
    override func intervalDidEnd(for activity: DeviceActivityName) {
        super.intervalDidEnd(for: activity)
        store.shield.applications = nil
        store.shield.applicationCategories = nil
        store.shield.webDomains = nil
        // Handle the end of the interval.
    }
    
    override func eventDidReachThreshold(_ event: DeviceActivityEvent.Name, activity: DeviceActivityName) {
        super.eventDidReachThreshold(event, activity: activity)
        
        // Handle the event reaching its threshold.
    }
    
    override func intervalWillStartWarning(for activity: DeviceActivityName) {
        super.intervalWillStartWarning(for: activity)
        
        // Handle the warning before the interval starts.
    }
    
    override func intervalWillEndWarning(for activity: DeviceActivityName) {
        super.intervalWillEndWarning(for: activity)
        
        // Handle the warning before the interval ends.
    }
    
    override func eventWillReachThresholdWarning(_ event: DeviceActivityEvent.Name, activity: DeviceActivityName) {
        super.eventWillReachThresholdWarning(event, activity: activity)
        
        // Handle the warning before the event reaches its threshold.
    }
}
