import UIKit
import FamilyControls
import SwiftUI
import ComposeApp
import DeviceActivity
import ScreenTimeReportExtension


struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.MainViewController(
            nativeViewFactory: IOSNativeViewFactory.shared
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
       ComposeView()
           .ignoresSafeArea()

    }
}

class IOSNativeViewFactory: NativeViewFactory {
    let appBlocker = AppBlockerUtil()
    let state = AppBlockerState()
    
    func createSelectedAppsIconView(tokens: [String]) -> UIViewController {
        let report = ProfileEditorView(
            state: state,
            appBlocker: appBlocker,
            exclude: true
        ) {
             
        }.frame(maxWidth: .infinity, maxHeight: 100)
        return UIHostingController(rootView: report)
    }
    
    
    func createOnboardingUsageScreen() -> UIViewController {
        let calendar = Calendar.current
        let now = Date()
        let sevenDaysAgo = calendar.date(
            byAdding: .day,
            value: -7,
            to: calendar.startOfDay(for: now)
        )!

        // End = now
        let interval = DateInterval(start: sevenDaysAgo, end: now)

        // Filter for LAST 7 DAYS
        let filter = DeviceActivityFilter(
            segment: .daily(during: interval),
            users: .all,
            devices: .init([.iPhone])
        )
        let report = DeviceActivityReport(
            DeviceActivityReport.Context.socialMediaGuilt,
            filter: filter
        ).frame(maxWidth: .infinity, maxHeight: .infinity)
        return UIHostingController(rootView: report)
    }
    
    static var shared = IOSNativeViewFactory()
    func createNativeScreen() -> UIViewController {
        let calendar = Calendar.current
        let now = Date()
        let startOfDay = calendar.startOfDay(for: now)
        let filter = DeviceActivityFilter(
            segment: .daily(
                during: DateInterval(
                    start: startOfDay,
                    end: now
                )
            ),
            users: .all,
            devices: .all
        )
        let report = DeviceActivityReport(
            DeviceActivityReport.Context.socialMediaGuilt,
            filter: filter
        ).frame(width: 500, height: 500)
        return UIHostingController(rootView: report)
    }
}




