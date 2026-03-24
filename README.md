# Goal Tracker Android MVP

Android-first personal goal tracker MVP.

## What Works

- create a project
- edit a project
- set one focus project
- complete a project and save legacy
- show a support message on home
- show a focus-project home screen widget

## Build

Use Java 17:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH=$JAVA_HOME/bin:/opt/homebrew/bin:$PATH
export GRADLE_USER_HOME=$PWD/.gradle-home
gradle assembleDebug
```

APK output:

```bash
app/build/outputs/apk/debug/app-debug.apk
```

## Local Android SDK

This project expects:

```bash
sdk.dir=/opt/homebrew/share/android-commandlinetools
```

That path is already set in `local.properties`.

## Emulator Setup Used Here

Installed packages:

- `emulator`
- `platforms;android-35`
- `build-tools;35.0.0`
- `system-images;android-35;google_apis;arm64-v8a`

Created AVD:

- `GoalTrackerApi35`

Start the emulator:

```bash
export ANDROID_SDK_ROOT=/opt/homebrew/share/android-commandlinetools
export PATH=$ANDROID_SDK_ROOT/emulator:$ANDROID_SDK_ROOT/platform-tools:$PATH
emulator @GoalTrackerApi35 -no-window -no-snapshot -no-boot-anim
```

Install the APK:

```bash
export ANDROID_SDK_ROOT=/opt/homebrew/share/android-commandlinetools
export PATH=$ANDROID_SDK_ROOT/platform-tools:$PATH
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Launch the app:

```bash
adb shell am start -W -n com.goaltracker.app/.ui.MainActivity
```
