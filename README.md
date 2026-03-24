# 골 트래커 안드로이드 MVP

안드로이드 우선 개인용 골 트래커 MVP다.

## 현재 동작 기능

- 프로젝트 생성
- 프로젝트 수정
- 포커스 프로젝트 1개 지정
- 프로젝트 완료 후 레거시 저장
- 홈에서 레거시 기반 응원 문구 표시
- 포커스 프로젝트 홈 화면 위젯 1종

## 빌드

Java 17 기준:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export PATH=$JAVA_HOME/bin:/opt/homebrew/bin:$PATH
export GRADLE_USER_HOME=$PWD/.gradle-home
gradle assembleDebug
```

APK 출력 경로:

```bash
app/build/outputs/apk/debug/app-debug.apk
```

## 로컬 안드로이드 SDK

이 프로젝트는 아래 경로를 사용한다.

```bash
sdk.dir=/opt/homebrew/share/android-commandlinetools
```

해당 값은 `local.properties`에 이미 설정되어 있다.

## 이 프로젝트에서 사용한 에뮬레이터 환경

설치한 패키지:

- `emulator`
- `platforms;android-35`
- `build-tools;35.0.0`
- `system-images;android-35;google_apis;arm64-v8a`

생성한 AVD:

- `GoalTrackerApi35`

에뮬레이터 실행:

```bash
export ANDROID_SDK_ROOT=/opt/homebrew/share/android-commandlinetools
export PATH=$ANDROID_SDK_ROOT/emulator:$ANDROID_SDK_ROOT/platform-tools:$PATH
emulator @GoalTrackerApi35 -no-window -no-snapshot -no-boot-anim
```

APK 설치:

```bash
export ANDROID_SDK_ROOT=/opt/homebrew/share/android-commandlinetools
export PATH=$ANDROID_SDK_ROOT/platform-tools:$PATH
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

앱 실행:

```bash
adb shell am start -W -n com.goaltracker.app/.ui.MainActivity
```
