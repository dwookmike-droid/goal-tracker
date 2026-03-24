# Android Goal Tracker MVP Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build and package a real installable Android APK for a personal goal tracker MVP that supports multi-project tracking, legacy capture, support messaging, and one focus-project widget.

**Architecture:** Use a single Android app built with Kotlin and Jetpack Compose. Store data locally with Room, expose state through a simple repository and ViewModels, and drive the widget from the same persisted focus-project state. Keep navigation and UI shallow so the release can be completed in one day.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, Navigation Compose, Room, Android App Widget or Glance, Gradle Wrapper, JUnit, Android instrumentation smoke tests

---

## File Structure

Expected project structure:

- `settings.gradle.kts`
- `build.gradle.kts`
- `gradle.properties`
- `gradle/libs.versions.toml`
- `app/build.gradle.kts`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/.../GoalTrackerApp.kt`
- `app/src/main/java/.../data/`
- `app/src/main/java/.../ui/`
- `app/src/main/java/.../widget/`
- `app/src/test/`
- `app/src/androidTest/`

Core responsibilities:

- app shell and navigation
- local entities, DAO, and database
- repository and simple business logic
- Compose screens
- completion to legacy flow
- support message generator
- widget provider and update bridge

## Delivery Rules

- Ship only Android.
- Ship one widget only.
- Use only local persistence.
- Do not add login, sync, or analytics.
- Prefer deterministic templates over AI.
- Defer polish when it blocks APK generation.

### Task 1: Environment And Scaffold

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle.properties`
- Create: `gradle/libs.versions.toml`
- Create: `gradlew`
- Create: `gradlew.bat`
- Create: `gradle/wrapper/gradle-wrapper.properties`
- Create: `app/build.gradle.kts`
- Create: `app/src/main/AndroidManifest.xml`

- [ ] **Step 1: Verify toolchain prerequisites**

Run: `java -version`
Expected: Java available.

- [ ] **Step 2: Confirm Android SDK location**

Run: `which sdkmanager`
Expected: `sdkmanager` available.

- [ ] **Step 3: Generate or add Gradle wrapper**

Run: `gradle wrapper --gradle-version 8.10.2`
Expected: wrapper files created.

If local `gradle` is broken, use a fallback installation path or approved environment bootstrap before continuing.

- [ ] **Step 4: Create the Android app shell**

Add minimal Gradle config for:

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}
```

- [ ] **Step 5: Add compile and target SDK config**

Use one stable SDK level already available locally if possible.

- [ ] **Step 6: Run a first Gradle help command**

Run: `./gradlew help`
Expected: build logic resolves successfully.

### Task 2: Data Layer

**Files:**
- Create: `app/src/main/java/.../data/model/Project.kt`
- Create: `app/src/main/java/.../data/model/Milestone.kt`
- Create: `app/src/main/java/.../data/model/LegacyRecord.kt`
- Create: `app/src/main/java/.../data/db/GoalTrackerDatabase.kt`
- Create: `app/src/main/java/.../data/db/ProjectDao.kt`
- Create: `app/src/main/java/.../data/repo/GoalTrackerRepository.kt`
- Test: `app/src/test/java/.../SupportMessageGeneratorTest.kt`

- [ ] **Step 1: Write failing support logic tests**

```kotlin
@Test
fun returns_completed_count_message_when_history_exists() {
    val result = SupportMessageGenerator.fromLegacy(
        completedCount = 3,
        latestLesson = null
    )
    assertThat(result).contains("3")
}
```

- [ ] **Step 2: Run unit test to verify failure**

Run: `./gradlew testDebugUnitTest`
Expected: FAIL because generator does not exist.

- [ ] **Step 3: Implement entities and support generator**

Keep fields limited to the MVP spec.

- [ ] **Step 4: Implement Room database and DAO**

Support:

- create project
- update project
- mark completed
- store legacy record
- fetch focus project
- fetch active projects
- fetch completed projects

- [ ] **Step 5: Re-run unit tests**

Run: `./gradlew testDebugUnitTest`
Expected: PASS for generator tests.

### Task 3: App Shell And Navigation

**Files:**
- Create: `app/src/main/java/.../GoalTrackerApp.kt`
- Create: `app/src/main/java/.../ui/navigation/GoalTrackerNavGraph.kt`
- Create: `app/src/main/java/.../ui/theme/`
- Create: `app/src/main/java/.../ui/MainActivity.kt`

- [ ] **Step 1: Create a failing navigation smoke test**

Test that the app can start and the home title is visible.

- [ ] **Step 2: Run instrumentation test to verify failure**

Run: `./gradlew connectedDebugAndroidTest`
Expected: FAIL until app shell exists.

- [ ] **Step 3: Implement activity, theme, and nav graph**

Routes:

- home
- projects
- project detail
- edit project
- complete project
- legacy

- [ ] **Step 4: Re-run smoke test**

Run: `./gradlew connectedDebugAndroidTest`
Expected: PASS for basic launch.

### Task 4: Project List And Create Flow

**Files:**
- Create: `app/src/main/java/.../ui/projects/ProjectsScreen.kt`
- Create: `app/src/main/java/.../ui/projects/EditProjectScreen.kt`
- Create: `app/src/main/java/.../ui/projects/ProjectsViewModel.kt`
- Test: `app/src/androidTest/java/.../ProjectsScreenTest.kt`

- [ ] **Step 1: Write failing UI test for project creation**

Assert a user can open create flow and save a project.

- [ ] **Step 2: Run targeted test and verify failure**

Run: `./gradlew connectedDebugAndroidTest`
Expected: FAIL because screen is missing.

- [ ] **Step 3: Implement project list**

Must show:

- focus project badge if selected
- active projects
- completed projects entry point

- [ ] **Step 4: Implement create and edit form**

Fields:

- title
- intent
- optional initial progress

- [ ] **Step 5: Re-run instrumentation tests**

Run: `./gradlew connectedDebugAndroidTest`
Expected: PASS for creation flow.

### Task 5: Project Detail And Focus Logic

**Files:**
- Create: `app/src/main/java/.../ui/projectdetail/ProjectDetailScreen.kt`
- Create: `app/src/main/java/.../ui/projectdetail/ProjectDetailViewModel.kt`
- Test: `app/src/androidTest/java/.../ProjectDetailScreenTest.kt`

- [ ] **Step 1: Write failing UI test for setting focus project**

- [ ] **Step 2: Run test and verify failure**

Run: `./gradlew connectedDebugAndroidTest`
Expected: FAIL because focus action is missing.

- [ ] **Step 3: Implement detail screen**

Must support:

- view intent
- update progress
- add milestone
- set focus project
- start completion flow

- [ ] **Step 4: Ensure only one focus project exists**

Repository should clear previous focus before setting a new one.

- [ ] **Step 5: Re-run tests**

Run: `./gradlew connectedDebugAndroidTest`
Expected: PASS for focus and progress updates.

### Task 6: Completion And Legacy Flow

**Files:**
- Create: `app/src/main/java/.../ui/legacy/CompleteProjectScreen.kt`
- Create: `app/src/main/java/.../ui/legacy/LegacyScreen.kt`
- Create: `app/src/main/java/.../ui/legacy/LegacyViewModel.kt`
- Test: `app/src/androidTest/java/.../CompleteProjectFlowTest.kt`

- [ ] **Step 1: Write failing UI test for completion flow**

Assert completing a project requires legacy inputs and moves it to legacy.

- [ ] **Step 2: Run test and verify failure**

Run: `./gradlew connectedDebugAndroidTest`
Expected: FAIL before flow exists.

- [ ] **Step 3: Implement completion screen**

Required fields:

- achievement summary
- lesson learned

- [ ] **Step 4: Persist completed status and legacy record together**

Use one transaction to avoid mismatched state.

- [ ] **Step 5: Implement legacy list screen**

Show:

- project title
- achievement
- lesson

- [ ] **Step 6: Re-run tests**

Run: `./gradlew connectedDebugAndroidTest`
Expected: PASS for completion flow.

### Task 7: Home Screen And Support Message

**Files:**
- Create: `app/src/main/java/.../ui/home/HomeScreen.kt`
- Create: `app/src/main/java/.../ui/home/HomeViewModel.kt`
- Test: `app/src/androidTest/java/.../HomeScreenTest.kt`

- [ ] **Step 1: Write failing test for home support line**

Assert home shows support message when legacy exists.

- [ ] **Step 2: Run test and verify failure**

Run: `./gradlew connectedDebugAndroidTest`
Expected: FAIL before support UI exists.

- [ ] **Step 3: Implement home screen**

Must show:

- focus project card
- active project list
- support message block

- [ ] **Step 4: Connect support generator to latest legacy data**

- [ ] **Step 5: Re-run tests**

Run: `./gradlew connectedDebugAndroidTest`
Expected: PASS for home presentation.

### Task 8: Widget

**Files:**
- Create: `app/src/main/java/.../widget/FocusProjectWidget.kt`
- Create: `app/src/main/java/.../widget/FocusProjectWidgetReceiver.kt`
- Modify: `app/src/main/AndroidManifest.xml`
- Test: `app/src/test/java/.../WidgetStateMapperTest.kt`

- [ ] **Step 1: Write failing unit test for widget state mapping**

Assert focus project plus support text maps into display model.

- [ ] **Step 2: Run test and verify failure**

Run: `./gradlew testDebugUnitTest`
Expected: FAIL before mapper exists.

- [ ] **Step 3: Implement widget provider**

Display:

- project title
- progress
- support line

- [ ] **Step 4: Trigger widget refresh after data changes**

Keep it simple: refresh on project save, focus change, and completion.

- [ ] **Step 5: Re-run tests**

Run: `./gradlew testDebugUnitTest`
Expected: PASS for widget mapping.

### Task 9: Release Pass

**Files:**
- Modify: build files as needed for release signing or debug-release packaging
- Create: `README.md`

- [ ] **Step 1: Run the full unit test suite**

Run: `./gradlew testDebugUnitTest`
Expected: PASS

- [ ] **Step 2: Run instrumentation smoke tests**

Run: `./gradlew connectedDebugAndroidTest`
Expected: PASS

- [ ] **Step 3: Build debug APK**

Run: `./gradlew assembleDebug`
Expected: APK generated in `app/build/outputs/apk/debug/`

- [ ] **Step 4: Install or verify package artifact**

Run: `adb install -r app/build/outputs/apk/debug/app-debug.apk`
Expected: install success if device or emulator is available.

- [ ] **Step 5: Smoke-check critical flows**

Validate manually:

- create project
- set focus
- update progress
- complete project
- view legacy
- widget reads focus project

## Team Split For Today

### Design Team Stream

- [ ] Lock Android visual direction in under 30 minutes.
- [ ] Deliver final screen specs for Home, Projects, Project Detail, and Complete Project.
- [ ] Deliver one widget spec.
- [ ] Freeze tokens and component rules.
- [ ] Stop design churn after engineering implementation starts.

### Engineering Team Stream

- [ ] Scaffold app and unblock build.
- [ ] Build data layer and project CRUD.
- [ ] Build focus logic and completion flow.
- [ ] Build home support message.
- [ ] Build widget.
- [ ] Produce debug APK.

## Critical Risks

- Broken local Gradle installation may block wrapper generation.
- Missing Android SDK path or emulator may block final verification.
- Widget implementation can slip and should be cut last only if APK delivery is at risk.

## Cut List If Schedule Slips

Cut in this order:

1. milestone add UI
2. project note field
3. completed projects rich detail
4. support line on widget
5. legacy list polish

Do not cut:

- create project
- set focus
- complete project
- legacy capture
- support line on home
- APK build
