# Z Launcher

A minimal, distraction-free Android launcher with a dark premium theme.

## What Is This?

Z Launcher replaces your default home screen with a clean, calm interface designed to reduce digital distractions. It shows only what matters — a clock, an inspirational quote, and the apps you actually use.

## Features

### Draggable Layout
- **Long press and drag** any element to rearrange your home screen:
  - Clock & date
  - Daily inspirational quote
  - Your selected app list
  - App widgets
- Positions are saved automatically

### Dark Premium Theme
- Deep black background
- Soft white text with green accents
- Clean, minimal, no visual noise

### Custom Wallpaper
- Pick any image from your gallery
- **Adjustable brightness** slider — control how dark/bright the wallpaper overlay is
- Wallpaper is copied to app storage (no permission issues)

### Daily Inspirational Quote
- New quote every day (70+ quotes)
- Displayed in italics
- No author shown

### Clock Customization
- 5 styles: Thin, Light, Bold, Monospace, Digital
- 6 colors: White, Green, Red, Blue, Yellow, Pink

### Focus Mode
- Hides all apps and widgets
- Shows only clock + timer
- Timer counts your focus duration
- Exit button to return to normal

### App Widgets
- Add any widget from installed apps
- Drag it anywhere on screen

### Home App Selection
- Choose which apps appear on your home screen
- Only essential apps, nothing else

### Switch to Default Launcher
- Password-protected button in Settings to exit Z Launcher
- First use: set a password (with confirmation)
- Subsequent uses: enter password to authenticate
- On success: opens the system home picker so you can switch back to your default launcher

## How to Use

1. **Open Z Launcher** from your app drawer
2. **Tap the gear icon** (bottom right) to open Settings
3. **Select Home Apps** — choose which apps to show
4. **Pick a Wallpaper** — adjust brightness with the slider
5. **Customize Clock** — change style and color
6. **Add a Widget** — optional
7. **Arrange** — long press and drag any element to your preferred position
8. **Focus Mode** — tap the clock icon (bottom left) to enter focus mode
9. **Switch to Default Launcher** — enter your password to exit Z Launcher and return to your previous launcher

### Set as Default Launcher
1. Settings → Apps → Default apps → Home app
2. Select "Z Launcher"
3. Press home — you're now on Z Launcher

## Tech Stack

- **Language:** Kotlin
- **UI:** Android XML layouts, RecyclerView, ConstraintLayout
- **Storage:** SharedPreferences (settings) + Internal Storage (wallpaper)
- **Widgets:** AppWidgetHost + AppWidgetManager
- **Min SDK:** Android 8.0 (API 26)
- **Target SDK:** Android 14 (API 34)

## Project Structure

```
├── DualProfiles/
│   ├── app/src/main/
│   │   ├── java/com/example/zenlauncher/
│   │   │   ├── MainActivity.kt          # Home screen (clock, quote, apps, widgets)
│   │   │   ├── AppSelectionActivity.kt  # Choose which apps appear
│   │   │   ├── SettingsActivity.kt      # Wallpaper, clock, brightness, widget settings
│   │   │   ├── WidgetPickerActivity.kt  # Pick a widget to add
│   │   │   ├── PasswordAuthActivity.kt  # Password protection for switching launcher
│   │   │   ├── adapter/
│   │   │   │   └── AppListAdapter.kt    # RecyclerView adapter
│   │   │   ├── data/
│   │   │   │   ├── AppRepository.kt     # Installed apps helper
│   │   │   │   └── QuoteRepository.kt   # 70+ daily quotes
│   │   │   ├── model/
│   │   │   │   ├── AppItem.kt           # App data class
│   │   │   │   └── Quote.kt             # Quote data class
│   │   │   └── util/
│   │   │       ├── AnimationHelper.kt   # Fade animations
│   │   │       └── PreferenceHelper.kt  # SharedPreferences + positions
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml        # Home screen layout
│   │   │   │   ├── activity_settings.xml    # Settings layout
│   │   │   │   ├── activity_app_selection.xml
│   │   │   │   ├── activity_password_auth.xml  # Password auth screen layout
│   │   │   │   └── item_app_list.xml
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   └── themes.xml
│   │   │   └── drawable/
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── gradlew / gradlew.bat
└── README.md
```

## Build from Terminal

```bash
# Build
./gradlew assembleDebug

# Install on connected device
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch
adb shell monkey -p com.example.zenlauncher -c android.intent.category.LAUNCHER 1
```

## Known Notes

- On first launch, go to Settings and select your apps before they appear
- Wallpaper brightness: 0% = very dark overlay, 100% = no overlay (full brightness)
- Clock and quote start at center — drag them anywhere you want
- Widget drag: long press the widget itself to move it

## License

Personal use.
