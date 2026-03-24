# Goal Tracker Design

## Product Definition

Goal Tracker is a personal iPhone-first app for running multiple projects at the same time while using completed projects as a source of motivation for current work.

This product is not just a habit tracker or a task list. Its core angle is:

- active project management
- legacy preservation for completed projects
- emotional support generated from past achievements
- widget-first daily visibility

## Problem

Existing goal trackers are usually strong in one of these areas:

- daily execution
- habits and streaks
- project planning
- team collaboration

But they rarely connect finished projects back into the user's current momentum. Completed work is usually archived, hidden, or reduced to a completion count.

The gap is a system where:

- a finished project remains part of the user's identity
- past effort becomes visible proof for current effort
- the home screen keeps this motivation active through widgets

## Target User

Primary target for v1:

- individual users
- iPhone users
- people running several personal projects in parallel
- creators, builders, students, founders, and self-directed workers

User profile:

- often has more than one meaningful project
- wants visible progress, not only tasks
- wants completed projects to remain meaningful
- responds to emotional reinforcement, not only productivity metrics

## Product Principles

1. Projects over tasks
The product centers on project identity and progress, not just isolated to-dos.

2. Legacy is active, not passive
Completed projects should continue to influence the present.

3. Widgets are part of the product, not an add-on
The home screen should carry the product's core value.

4. Ten-plus projects must remain manageable
The app should support users with many concurrent or paused projects without feeling crowded.

5. Motivation should feel earned
Support messages must come from the user's real history, not generic quotes.

## MVP Scope

The first release includes five core areas:

### 1. Home

Purpose:

- show the user's current momentum at a glance
- connect active work with legacy support

Contents:

- primary focus project
- active projects summary
- quick daily check-in
- legacy support message
- progress snapshot

### 2. Project List

Purpose:

- let users manage many projects with low friction

Contents:

- sections for active, paused, and completed projects
- support for more than 10 projects
- sort by priority, recency, or progress
- quick project creation

### 3. Project Detail

Purpose:

- make each project feel like an owned journey, not a flat record

Contents:

- title and intent
- status and timeline
- progress value
- milestone list
- notes
- optional daily focus entry

### 4. Legacy Vault

Purpose:

- preserve ended projects as meaningful assets

Contents:

- completed project archive
- key achievement
- lesson learned
- proud result
- symbolic line or quote

### 5. Widgets

Purpose:

- keep motivation and direction visible outside the app

Initial widget set:

- Focus Project Widget
- Progress Ring Widget
- Legacy Support Widget

## Core Feature Details

### Feature A. Widget System

The widget system is one of the main reasons to choose this app over a generic tracker.

Requirements:

- show one selected focus project
- show current progress at a glance
- optionally show a legacy-derived support line
- refresh whenever project state changes

Widget concepts:

1. Focus Project
- project name
- current percentage
- today's short focus text

2. Progress Ring
- circular progress
- milestone count or days left

3. Legacy Support
- short line generated from previous completed projects
- examples:
  - "You already finished 7 projects."
  - "This project follows the path of Design Sprint Archive."
  - "Past you already solved a similar phase."

### Feature B. Legacy Management

This is the product's clearest differentiator.

When a project is completed, it does not disappear into storage. It becomes a legacy object with narrative meaning.

Each completed project should capture:

- what was achieved
- what was learned
- what matters most in hindsight
- what result best represents the project
- one symbolic supporting phrase

Legacy must be usable in two ways:

- archive browsing
- support generation for active projects

### Feature C. Support Engine

The support engine turns legacy records into present-tense encouragement.

v1 does not need AI generation. Template-based composition is sufficient.

Inputs:

- count of completed projects
- matching category or theme
- similar project history
- recent wins
- user-written symbolic phrases

Outputs:

- short support messages on home
- support messages on project detail
- support messages on widget

Design rule:

- support should feel grounded in prior evidence
- avoid generic motivation language

### Feature D. Multi-Project Handling

The product must comfortably support 10 or more projects.

Requirements:

- visible status separation: active, paused, completed
- lightweight switching between projects
- no forced single-goal mode
- no clutter collapse when project count grows

Recommended management model for v1:

- one focus project
- many active projects
- optional paused projects
- completed projects move into legacy automatically

## Suggested Information Architecture

Bottom navigation for v1:

- Home
- Projects
- Legacy
- Settings

Screen map:

1. Home
- focus project card
- active project strip
- support message block
- quick check-in entry

2. Projects
- active tab
- paused tab
- completed tab
- create project flow

3. Project Detail
- overview
- milestones
- notes
- support section

4. Legacy
- archive list
- legacy detail

5. Settings
- widget preferences
- support tone preferences
- sorting preferences

## Data Model

### Project

- id
- title
- intent
- category
- status
- startDate
- targetDate
- completedDate
- progress
- isFocus
- priority

### Milestone

- id
- projectId
- title
- isDone
- order

### ProjectNote

- id
- projectId
- content
- createdAt

### LegacyRecord

- id
- projectId
- achievement
- lesson
- proudResult
- symbolicLine
- createdAt

### SupportMessage

- id
- currentProjectId
- sourceLegacyIds
- type
- text
- createdAt

## User Flow

### New User Flow

1. open app
2. create first project
3. optionally create more projects
4. mark one as focus project
5. use home view and widget as daily entry point

### Completion Flow

1. user completes project
2. app asks for legacy summary
3. project moves to completed
4. legacy record is created
5. support engine can now use it

### Daily Use Flow

1. user sees widget
2. user opens focus project
3. user updates progress or milestone
4. home refreshes support and status

## v1 Non-Goals

These should not be in the first release:

- team collaboration
- web app
- Android release
- AI-generated long coaching
- complex analytics dashboards
- deep integrations with calendar or task apps

## Competitive Positioning

Compared with Todoist, TickTick, ClickUp, and Asana:

- weaker on team collaboration
- weaker on enterprise planning
- stronger on personal narrative and emotional continuity

Compared with habit trackers:

- weaker on streak gamification
- stronger on project identity, archives, and meaning

## Recommended Build Order

1. data model and local persistence
2. project list and project detail
3. project completion to legacy flow
4. support engine
5. widget set
6. visual polish and onboarding

## Success Criteria For MVP

The MVP succeeds if a user can:

- manage more than 10 projects without confusion
- set and switch a focus project
- complete a project and preserve it as legacy
- see support from past work on the home screen
- use at least one meaningful widget every day

## Open Risks

1. Legacy support could feel gimmicky if messages are too generic.
2. Too many active projects could create noise if hierarchy is weak.
3. Widget value drops if data entry inside the app is too slow.

## Recommendation

The strongest version of this product is not a broad productivity app. It is a focused personal project tracker with an unusual emotional mechanic:

"Finished projects do not disappear. They keep pushing current projects forward."

That idea should stay central in all UX and implementation decisions.
