import SwiftUI

struct PhoneUsageView: View {
    
    let config: PhoneUsageStatsModel
    
    var body: some View {
        ZStack {
            Color(hex: "0D0D0D").ignoresSafeArea()
            
            VStack(spacing: 40) {
                // Header
                VStack(spacing: 30) {
                    Text("you will use your phone")
                        .customFont(.regular, 20)
                        .foregroundColor(.white)
                    
                    HStack(alignment: .firstTextBaseline, spacing: 4) {
                        Text("\(config.numberOfDaysInYears) days")
                            .customFont(.bold, 26)
                            .foregroundColor(.white)
                        Text("in a year")
                            .customFont(.regular, 20)
                            .foregroundColor(.white)
                    }
                }

                VStack(spacing: 30) {
                    ForEach(config.items.indices, id: \.self) { index in
                        let item = config.items[index]

                        StatCard(
                            titleIcon: item.titleIcon,
                            title: item.title,
                            value: item.value,
                            subtitle: item.subtitle,
                            backgroundColor: item.backgroundColor,
                            borderColor: item.borderColor,
                            valueColor: item.borderColor,
                            icons: item.apps,
                            extraCount: max(0, item.apps.count - 2)
                        )
                    }
                }
                .padding(.horizontal, 16)
                
                Spacer()
            }
        }
    }
}

struct StatCard: View {
    let titleIcon: String
    let title: String
    let value: String
    let subtitle: AttributedString?
    let backgroundColor: Color
    let borderColor: Color
    let valueColor: Color
    let icons: [String]
    let extraCount: Int?
    
    @State private var url: URL?
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            // Main card
            HStack {
                HStack(spacing: 8) {
                    if let imageName = AppIconProvider.image(for: titleIcon) {
                        Image(imageName)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 20, height: 20)
                            .cornerRadius(6)
                    }
                    
                    Text(title)
                        .customFont(.medium, 12)
                        .foregroundColor(.white)
                }
                
                Spacer()
                
                HStack(spacing: 8) {
                    Text(value)
                        .customFont(.semiBold, 12)
                        .foregroundColor(valueColor)
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
            .background(backgroundColor)
            .overlay(
                RoundedRectangle(cornerRadius: 14)
                    .stroke(borderColor, lineWidth: 2)
            )
            .cornerRadius(14)
            
            // Subtitle
            if let subtitle = subtitle {
                Text(subtitle)
                    .customFont(.medium, 10)
                    .foregroundColor(Color.white.opacity(0.3))
                    .padding(.leading, 10)
            }
        }
        .task {
            url = await AppIconFetcher.shared.fetchIconURL(bundleId: titleIcon)
        }
    }
}

// Color extension to support hex colors
extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 3: // RGB (12-bit)
            (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6: // RGB (24-bit)
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8: // ARGB (32-bit)
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (1, 1, 1, 0)
        }

        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue:  Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
}
