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
    
    func createSuccessScreenUsage(startTime: Int64, endTime: Int64) -> UIViewController {
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMM yyyy, hh:mm a"
        let startDate = Date(timeIntervalSince1970: TimeInterval(startTime) / 1000)
        let endDate = Date(timeIntervalSince1970: TimeInterval(endTime) / 1000)
        
        print("start date: \(formatter.string(from: startDate))")
        print("end date: \(formatter.string(from: endDate))")
//
        let interval = DateInterval(
           start: startDate,
           end: endDate
        )

        let filter = DeviceActivityFilter(
            segment: .hourly(during: interval), // or .daily if you want aggregation
            users: .all,
            devices: .init([.iPhone])
        )
        let report = DeviceActivityReport(
            DeviceActivityReport.Context.successScreen,
            filter: filter
        )
            .frame(maxWidth: .infinity, maxHeight: 500)
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




