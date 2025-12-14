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
    
    let state = AppBlockerState.shared
    let appBlocker = AppBlockerUtil()
    
    var profileHost: UIHostingController<ProfileEditorView>?
    
    @objc public func selectApps(exclude: Bool, selectApps: @escaping ([String]) -> Void) {
        self.profileHost?.view.removeFromSuperview()
        self.profileHost = nil
        
        let report = ProfileEditorView(
            state: state,
            appBlocker: appBlocker,
            exclude: exclude,
            onSave: {
                let result = self.state.activitySelection.applicationTokens.map { app in ""}
                let result2 = self.state.activitySelection.webDomainTokens.map { app in ""}
                selectApps(result + result2)
            }
        )

        let host = UIHostingController(rootView: report)
        host.view.frame = CGRect(x: -100, y: -100, width: 1, height: 1)
        profileHost = host
        
        UIApplication.shared.windows.first?.rootViewController?.view.addSubview(host.view)
    }
    
    
    @objc public func stopBlocking() {
        appBlocker.deactivateRestrictions()
    }
    
    
    @objc public func startBlocking(time: Int) {
        appBlocker.activateRestrictions(time: time, selection: state.activitySelection)
    }
    
    
    @objc public func requestPermission(
        onSuccess: @escaping (Bool) -> Void,
        onError: @escaping (String) -> Void
    ) {
        Task {
            do {
                try await AuthorizationCenter.shared.requestAuthorization(for: .individual)
                await MainActor.run {
                    onSuccess(true)
                }
            } catch let error {
                await MainActor.run {
                    onError(error.localizedDescription)
                }
            }
        }
    }
}


@available(iOS 16.0, *)
extension DeviceActivityReport.Context {
    static let socialMediaGuilt = Self("socialMediaGuilt")   // ← Must be identical everywhere
    static let successScreen = Self("successScreen")  // ← Must be identical everywhere
}



@available(iOS 16.0, *)
struct ProfileEditorView: View {
    @ObservedObject var state: AppBlockerState
    let appBlocker: AppBlockerUtil
    let exclude: Bool
    let onSave: () -> Void
    
    @State var showSheet: Bool = true
    
    
    init(state: AppBlockerState, appBlocker: AppBlockerUtil, exclude: Bool, onSave: @escaping () -> Void) {
        self.state = state
        self.appBlocker = appBlocker
        self.exclude = exclude
        self.onSave = onSave
        self.showSheet = showSheet
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            FamilyActivityPicker(selection: $state.activitySelection)
        }
        .background(Color(hex: "#0D0D0D"))
        .familyActivityPicker(
            headerText: exclude ? "select apps to exclude from blocking" : "select apps to block",
            footerText: "",
            isPresented: $state.isPickerPresented,
            selection: $state.activitySelection
        )
        .onChange(of: state.activitySelection) { newSelection in
            state.activitySelection = newSelection
        }
        .sheet(isPresented: $showSheet, onDismiss: {
            onSave()
        }) {
            AppsBlockedBottomSheet(
                onAddClick: {
                    state.isPickerPresented = true
                },
                dismissClick: {
                    showSheet = false
                },
                state: state,
            )
        }
    }
}

@available(iOS 16.0, *)
struct TokenIconView: View {
    let token: ApplicationToken

    var body: some View {
        Label(token)
            .labelStyle(.iconOnly)
            .padding(4)
            .frame(width: 32, height: 32)  // Icon size
            .clipShape(Circle())
    }
}


@available(iOS 16.0, *)
class AppBlockerUtil { // Continuing the simplified class
    let store = ManagedSettingsStore(named: ManagedSettingsStore.Name("demoRestrictions"))
    let center = DeviceActivityCenter()
    let monitor = DeviceActivityMonitor()
    // Define a unique name for your activity
    static let activityName = DeviceActivityName("demoRestrictions")
    // ... (applyRestrictions, removeRestrictions from above) ...
    func startMonitoringSchedule(time: Int, selection: FamilyActivitySelection) {
        // Define a schedule. This example is 24/7, repeating daily.
        let now = Calendar.current.dateComponents([.hour, .minute], from: Date())
        let endDate = Calendar.current.date(byAdding: .minute, value: time, to: Date())!
        let end = Calendar.current.dateComponents([.hour, .minute], from: endDate)

        let schedule = DeviceActivitySchedule(
            intervalStart: now,
            intervalEnd: end,
            repeats: false,
            warningTime: nil
        )
        print("Starting DeviceActivity monitoring for schedule...")
        do {
            // Start monitoring. This tells the system to check the 'store'
            // associated with this activity during the 'schedule'.
            try center.startMonitoring(
                Self.activityName,
                during: schedule
            )
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
    func activateRestrictions(time: Int, selection: FamilyActivitySelection) {
        applyRestrictions(selection: selection) // Step 2: Define rules
        startMonitoringSchedule(time: time, selection: selection)             // Step 3: Activate schedule
    }
    
    func applyRestrictions(selection: FamilyActivitySelection) {
        let applicationTokens = selection.applicationTokens
        let categoryTokens = selection.categoryTokens
        let webTokens = selection.webDomainTokens
        store.shield.applications = applicationTokens.isEmpty ? nil : applicationTokens
        store.shield.applicationCategories = categoryTokens.isEmpty ? nil : .specific(categoryTokens)
        store.shield.webDomains = webTokens.isEmpty ? nil : webTokens
//        store.application.blockedApplications = selection.applications
//        store.application.denyAppRemoval = true
//        store.application.denyAppInstallation = true
//        store.passcode.lockPasscode = true
//        store.shield.applicationCategories = .all()
    }
    
    // Combined Deactivation Logic
    func deactivateRestrictions() {
        removeRestrictions() // Step 2: Clear rules
        stopMonitoring()     // Step 3: Deactivate schedule
    }
    func removeRestrictions() {
        print("Removing restrictions...")
        store.shield.applications = nil
        store.shield.applicationCategories = nil
        store.shield.webDomains = nil
        store.application.blockedApplications = nil
        store.application.denyAppRemoval = false
        store.application.denyAppInstallation = false
        store.passcode.lockPasscode = false
        // NOTE: Also need to stop DeviceActivity monitoring.
    }
}




enum FontWeight {
    case light
    case regular
    case medium
    case semiBold
    case bold
    case extraBold
}

extension Font {
    static let customFont: (FontWeight, CGFloat) -> Font = { fontType, size in
        switch fontType {
        case .light:
            Font.custom("Montserrat-Light", size: size)
        case .regular:
            Font.custom("Montserrat-Regular", size: size)
        case .medium:
            Font.custom("Montserrat-Medium", size: size)
        case .semiBold:
            Font.custom("Montserrat-SemiBold", size: size)
        case .bold:
            Font.custom("Montserrat-Bold", size: size)
        case .extraBold:
            Font.custom("Montserrat-ExtraBold", size: size)
        }
    }
}

extension Text {
    func customFont(_ fontWeight: FontWeight? = .regular, _ size: CGFloat? = nil) -> Text {
        return self.font(.customFont(fontWeight ?? .regular, size ?? 16))
    }
}


extension Color {
    init(hex: String) {
        var hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)

        let r, g, b, a: UInt64
        switch hex.count {
        case 3: // RGB (12-bit)
            (r, g, b, a) = (
                (int >> 8) * 17,
                (int >> 4 & 0xF) * 17,
                (int & 0xF) * 17,
                255
            )
        case 6: // RGB (24-bit)
            (r, g, b, a) = (
                int >> 16,
                int >> 8 & 0xFF,
                int & 0xFF,
                255
            )
        case 8: // ARGB (32-bit)
            (r, g, b, a) = (
                int >> 16 & 0xFF,
                int >> 8 & 0xFF,
                int & 0xFF,
                int >> 24 & 0xFF
            )
        default:
            (r, g, b, a) = (0, 0, 0, 255)
        }

        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue: Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
}
