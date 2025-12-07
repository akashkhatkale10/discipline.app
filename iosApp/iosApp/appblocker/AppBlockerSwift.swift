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
    
    let appBlocker = AppBlockerUtil()
    
    @objc public func selectApps(exclude: Bool) -> []
    
    @objc public func stopBlocking() {
        appBlocker.deactivateRestrictions()
    }
    
    @objc public func startBlocking() {

        let report = ProfileEditorView(appBlocker: appBlocker)
        
        let host = UIHostingController(rootView: report)
        host.view.frame = CGRect(x: 100, y: 100, width: 500, height: 300)
        UIApplication.shared.windows.first?.rootViewController?.view.addSubview(host.view)
    }
    
    
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
}


@available(iOS 16.0, *)
extension DeviceActivityReport.Context {
    static let socialMediaGuilt = Self("socialMediaGuilt")   // ← Must be identical everywhere
}

@available(iOS 16.0, *)
struct ProfileEditorView: View {
    let appBlocker: AppBlockerUtil
    // State to store the user's selection
    @State private var activitySelection = FamilyActivitySelection()
    // State to control the presentation of the picker
    @State private var isPickerPresented = false
    var body: some View {
        VStack {
            Text("Selected \(activitySelection.applicationTokens.count) apps, \(activitySelection.categoryTokens.count) categories, \(activitySelection.webDomainTokens.count) websites")
            Button("Select Apps & Websites") {
                isPickerPresented = true
            }
        }
        // The magic modifier!
        .familyActivityPicker(
            headerText: "select apps to block",
            footerText: "demo",
            isPresented: $isPickerPresented,
            selection: $activitySelection
        )
        .onChange(of: activitySelection) { newSelection in
            print("Selection Updated!")
            // In a real app, you'd likely save 'newSelection'
            // to your data model (like the 'BlockedProfiles' model).
            saveSelection(newSelection)
        }
    }
    func saveSelection(_ selection: FamilyActivitySelection) {
        // Placeholder: Implement saving logic here
        // e.g., update your SwiftData model:
        // try? BlockedProfiles.updateProfile(profile, in: context, selection: selection)
        print("Saving selection... \(selection.applications)")
        appBlocker.activateRestrictions(selection: selection)
    }
}

@available(iOS 16.0, *)
class AppBlockerUtil { // Continuing the simplified class
    let store = ManagedSettingsStore(named: ManagedSettingsStore.Name("demoRestrictions"))
    let center = DeviceActivityCenter()
    // Define a unique name for your activity
    static let activityName = DeviceActivityName("demoRestrictions")
    // ... (applyRestrictions, removeRestrictions from above) ...
    func startMonitoringSchedule(selectedApps: FamilyActivitySelection) {
        // Define a schedule. This example is 24/7, repeating daily.
        let now = Calendar.current.dateComponents([.hour, .minute], from: Date())
        let endDate = Calendar.current.date(byAdding: .minute, value: 5, to: Date())!
        let end = Calendar.current.dateComponents([.hour, .minute], from: endDate)
        
//        let event = DeviceActivityEvent(
//            applications: selectedApps.applicationTokens
//        )
//
//        let events: [DeviceActivityEvent.Name: DeviceActivityEvent] = [
//            .init("daily_limit"): event
//        ]

        let schedule = DeviceActivitySchedule(
            intervalStart: DateComponents(hour: 0, minute: 0),
            intervalEnd: DateComponents(hour: 23, minute: 59),
            repeats: true,
            warningTime: nil
        )
        print("Starting DeviceActivity monitoring for schedule...")
        do {
            // Start monitoring. This tells the system to check the 'store'
            // associated with this activity during the 'schedule'.
            try center.startMonitoring(Self.activityName, during: schedule)
            print("Monitoring started successfully.")
        } catch {
            print("Error starting DeviceActivity monitoring: \(error)")
        }
    }
    func stopMonitoring() {
        print("Stopping DeviceActivity monitoring...")
        // Stop monitoring for all activities or specify names
        center.stopMonitoring([Self.activityName])
        print("Monitoring stopped.")
    }
    // Combined Activation Logic (Similar to provided code)
    func activateRestrictions(selection: FamilyActivitySelection) {
        applyRestrictions(selection: selection) // Step 2: Define rules
        startMonitoringSchedule(selectedApps: selection)             // Step 3: Activate schedule
    }
    
    func applyRestrictions(selection: FamilyActivitySelection) {
//        store.shield.applications = selection.applicationTokens
        store.application.blockedApplications = selection.applications
        store.application.denyAppRemoval = true
        //store.application.denyAppInstallation = true
        store.passcode.lockPasscode = true
        store.shield.applicationCategories = .all()
    }
    
    // Combined Deactivation Logic
    func deactivateRestrictions() {
        removeRestrictions() // Step 2: Clear rules
        stopMonitoring()     // Step 3: Deactivate schedule
    }
    func removeRestrictions() {
        print("Removing restrictions...")
        // Clear the shield configuration
        store.shield.applications = nil
        store.shield.applicationCategories = nil
        store.shield.webDomains = nil
        print("Restrictions removed from ManagedSettingsStore.")
        // NOTE: Also need to stop DeviceActivity monitoring.
    }
}
