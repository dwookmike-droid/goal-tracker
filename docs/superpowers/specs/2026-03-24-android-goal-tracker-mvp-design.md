# Android Goal Tracker MVP Design

## Product Definition

This product is an Android-first personal goal tracker focused on running multiple projects while turning completed projects into visible support for current work.

The release target is a real installable APK by the end of 2026-03-24.

## Delivery Constraint

This is a one-day MVP release. Scope must be limited to features that can be designed, built, tested, and packaged today.

Because of that, the product must prioritize:

- working Android app over broad feature coverage
- stable local data over integrations
- one strong widget over multiple widget types
- simple support logic over AI generation

## Core Product Angle

Most goal trackers handle tasks, habits, or projects. This MVP should prove one specific difference:

"Completed projects stay alive as legacy and support current projects."

## Platform And Stack

- Platform: Android
- Release artifact: APK
- UI: Kotlin + Jetpack Compose
- Persistence: Room or equivalent local database
- Widget: Android App Widget via Glance or RemoteViews based on implementation speed

## MVP Users

- individual Android users
- people juggling multiple personal projects
- users who want motivation tied to their real history

## MVP Scope

The MVP includes only these user-facing areas.

### 1. Home

Home must answer three questions immediately:

- what is my focus project
- what else is active
- what proof do I have that I can finish this

Contents:

- focus project card
- active projects list
- one support message derived from completed projects
- quick progress update entry point

### 2. Projects

Project management must be minimal and reliable.

Supported actions:

- create project
- edit project
- mark project complete
- set focus project

Supported states:

- active
- completed

Not included in MVP:

- paused
- archived filters beyond completed list
- advanced tags or categories UI

### 3. Project Detail

Each project needs only the information required to feel real and trackable.

Fields:

- title
- intent
- progress percentage
- milestone list
- optional note

Actions:

- update progress
- add milestone
- complete project

### 4. Legacy Capture

Completion must trigger legacy capture before the project becomes completed history.

Required fields at completion:

- achievement summary
- lesson learned

Optional for MVP:

- proud result
- symbolic line

### 5. Widget

The MVP ships with one widget only:

- Focus Project Widget

Widget contents:

- focus project title
- progress percentage
- one short support line if legacy exists

## Data Model

### Project

- id
- title
- intent
- status
- progress
- isFocus
- createdAt
- updatedAt
- completedAt

### Milestone

- id
- projectId
- title
- isDone
- createdAt

### LegacyRecord

- id
- projectId
- achievement
- lesson
- createdAt

## Support Engine

The support engine must be deterministic and template-based.

Inputs:

- completed project count
- latest achievement text
- whether the user has any completed projects

Output examples:

- "You already finished 3 projects."
- "Past work proves you can finish this one too."
- "Last lesson: ship before it feels perfect."

Rules:

- keep messages under one short sentence
- use user-authored legacy where available
- no generic inspirational filler

## Information Architecture

Bottom navigation:

- Home
- Projects
- Legacy

Primary screens:

1. Home
2. Project List
3. Project Detail
4. Create/Edit Project
5. Complete Project / Legacy Capture
6. Legacy List

## Design Priorities

The UI should feel calm, direct, and Android-native.

Priorities:

- fast project scanning
- clear focus hierarchy
- low-friction completion flow
- strong contrast for widget readability

## Team Split

### Design Team

Design ownership for today:

- define Android visual direction
- produce four final screens:
  - Home
  - Project List
  - Project Detail
  - Complete Project / Legacy Capture
- define one widget layout
- define tokens:
  - colors
  - typography
  - spacing
  - card style
  - button style

Design deliverables should optimize for implementation speed, not exploration breadth.

### Engineering Team

Engineering ownership for today:

- scaffold Android app
- implement local persistence
- build project CRUD
- implement focus project logic
- implement completion to legacy flow
- implement support message logic
- implement home widget
- generate release-ready APK

## Build Order

1. app scaffold and navigation
2. local data layer
3. project list and create flow
4. project detail and progress updates
5. completion to legacy flow
6. home support message logic
7. widget
8. APK packaging and smoke test

## Non-Goals

Do not build these today:

- login
- sync
- cloud backup
- team collaboration
- multiple widget types
- analytics dashboard
- Android tablet optimization
- iOS

## Success Criteria

The MVP is successful today if:

- the APK installs on Android
- the user can create multiple projects
- one project can be marked as focus
- a project can be completed and turned into legacy
- home shows a support line based on legacy
- the widget shows the current focus project

## Risk Notes

1. The widget can become a release blocker if implemented too late.
2. Room plus widget refresh integration must stay simple.
3. Over-design from the design team will slow delivery.

## Recommendation

Ship the smallest version that proves the core idea:

"My past completed projects still help me move my current project forward."

Everything else can wait until after the first APK is in users' hands.
