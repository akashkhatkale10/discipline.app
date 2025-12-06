//
//  AppIconProvider.swift
//  iosApp
//
//  Created by Akash Khatkale on 06/12/25.
//
import SwiftUI

struct AppIconProvider {
    static let bundleToImage: [String: ImageResource] = [
        // meta
        "net.whatsapp.WhatsApp": .whatsapp,
        "net.whatsapp.WhatsAppSMB": .whatsappBusiness,
        "com.facebook.Messenger": .messenger,
        "com.facebook.Facebook": .facebook,
        "com.burbn.instagram": .instagram,
        "com.burbn.barcelona": .threads,
        
        // microsoft
        "com.microsoft.skype.teams": .microsoftTeams,
        "com.microsoft.Office.Outlook": .microsoftOutlook,
        
        
        "ph.telegra.Telegraph": .telegram,
        "com.atebits.Tweetie2": .twitter,
        "org.whispersystems.signal": .signal,
        "com.toyopagroup.picaboo": .snapchat,
        "olacabs.OlaCabs": .ola,
        "com.soundcloud.TouchApp": .soundcloud,
        "com.truesoftware.TrueCallerOther": .truecaller,
        
        // google
        "com.google.paisa": .googlePay,
        "com.google.Docs": .googleDrive1,
        "com.google.Drive": .googleDrive,
        "com.google.Sheets": .googleSheets,
        "com.google.photos": .googlePhotos,
        "com.google.GoogleMobile": .google,
        "com.google.Gmail": .gmail,
        "com.google.gemini": .googleGemini,
        "com.google.Maps": .googleMaps,
        "com.google.chrome.ios": .chrome,
        "com.google.ios.youtube": .youtube,
        "com.google.ios.youtubemusic": .youtubeMusic,
        "com.google.ios.youtubekids": .youtubeKids,
        "com.zhiliaoapp.musically": .tiktok,
        "com.google.GoogleDigitalEditions": .googleNews,
        
        
        "com.tinyspeck.chatlyio": .slack,
        "com.openai.chat": .chatgpt,
        "com.linkedin.LinkedIn": .linkedin,
        "pinterest": .pinterest,
        "com.canva.canvaeditor": .canva,
        "com.picsart.studio": .picsart,
        "com.strava.stravaride": .strava,
        "com.nike.nikeplus-gps": .nikeRunClub,
        
        // apple
        "com.apple.podcasts": .applePodcasts,
        "com.apple.photos": .gallery,
        "com.apple.DocumentsApp": .files,
        "com.apple.mobiletimer": .clock,
        "com.apple.calculator": .calculator,
        "com.apple.Music": .appleMusic,
        "com.apple.weather": .appleWeather,
        
        "com.netflix.Netflix": .netflix,
        "com.spotify.client": .spotify,
        "com.hammerandchisel.discord": .discord,
        "tv.twitch": .twitch,
        "com.getdropbox.Dropbox": .dropbox,
        "com.quora.app.mobile": .quora,
        "com.amazon.Amazon": .amazon,
        "com.amazon.aiv.AIVApp": .amazonPrimeVideo,
        "com.duolingo.DuolingoMobile": .duolingo,
        "com.airbnb.app": .airbnb,
        
        // indian
        "com.grofers.consumer": .blinkit,
        "bundl.swiggy": .swiggy,
        "com.bigbasket.mobileapp": .bigbasket,
        "com.zeptonow.customer": .zepto,
        "com.zomato.zomato": .zomato,
        "com.appflipkart.flipkart": .flipkart,
        "com.razorpay.payments.app": .razorpay,
        "com.reddit.Reddit": .reddit,
        "bike.rapido.customer": .rapido,
        "com.myntra.Myntra": .myntra,
        "com.meesho.Meesho": .meesho,
        "com.BhartiMobile.myairtel": .airtelThanks,
        "com.dreamplug.cred": .cred,
        "com.one97.paytm": .paytm,
        "com.phonepe.PhonePeApp": .phonepe,
        "com.nis.app": .inshorts,
        "com.angelbroking.angeleye": .angelOne,
        "com.nextbillion.groww": .groww,
        "com.Iphone.MMT": .makemytrip,
        "BookMyShow.com": .bookmyshow,
        "com.cleartrip.iphoneapp": .cleartrip,
        "in.startv.hotstar": .hotstar,
        "com.disney.disneyplus": .disney
    ]
    
    static func image(for bundleId: String) -> ImageResource? {
        return bundleToImage[bundleId] ?? nil
    }
}
